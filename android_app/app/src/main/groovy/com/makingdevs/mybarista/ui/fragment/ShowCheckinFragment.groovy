package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.*
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.Fluent
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.OnActivityResultGallery
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.activity.BaristaActivity
import com.makingdevs.mybarista.ui.activity.CheckInActivity
import com.makingdevs.mybarista.view.LoadingDialog
import com.makingdevs.mybarista.view.NoteDialog
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCheckinFragment extends Fragment implements OnActivityResultGallery {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "ShowCheckinFragment"
    private static final String CURRENT_CHECKIN = "checkin"
    private static final String CURRENT_CHECK_IN_ID = "check_in"
    private static final String ACTION_CHECK_IN = "action_check_in"
    private static final String CURRENT_CIRCLE_FLAVOR = "circle_flavor"


    ImageUtil mImageUtil1 = new ImageUtil()

    TextView mOrigin
    TextView mMethod
    TextView mPrice
    TextView mNote
    TextView mDateCreated
    TextView mBaristaName
    View itemView
    ImageButton mButtonNote
    User currentUser
    Button mBarista
    String mCheckinId
    Checkin checkin
    ImageButton mButtonEditCheckin
    ViewStub userActionsView
    Button shareCheckin
    ShareDialog shareDialog
    LoadingDialog loadingDialog = LoadingDialog.newInstance(R.string.message_sharing_photo)

    ShowCheckinFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(activity.getApplicationContext())
        callbackManager = CallbackManager.Factory.create()
        shareDialog = new ShareDialog(this)
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.fragment_show_chek_in, container, false)

        //Current User
        currentUser = mSessionManager.getUserSession(getContext())

        //Current Checkin
        checkin = getActivity().intent.extras.getSerializable(CURRENT_CHECKIN) as Checkin

        mCheckinId = getActivity().getIntent().getExtras().getString("checkin_id")
        if (!mCheckinId)
            throw new IllegalArgumentException("No arguments $mCheckinId")


        bindingViews()
        validateCheckinAuthor()
        setUserActions()

        itemView
    }

    private void validateCheckinAuthor() {
        if (checkin.author != currentUser.username) {
            mButtonEditCheckin.setVisibility(View.GONE)
            mBarista.setVisibility(View.GONE)
            mButtonNote.setVisibility(View.GONE)
            shareCheckin.setVisibility(View.GONE)
        }
    }


    @Override
    void onResume() {
        super.onResume()
        mCheckinManager.show(mCheckinId, onSuccess(), onError())
    }

    private void bindingViews() {
        userActionsView = (ViewStub) itemView.findViewById(R.id.stub_bottons)
        userActionsView.inflate()
        mOrigin = (TextView) itemView.findViewById(R.id.origin_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        mBaristaName = (TextView) itemView.findViewById(R.id.barista_name_data)
        showImage = (ImageView) itemView.findViewById(R.id.show_photo_checkin)
        mDateCreated = (TextView) itemView.findViewById(R.id.label_created)
        mButtonNote = (ImageButton) itemView.findViewById(R.id.btnNote)
        mBarista = (Button) itemView.findViewById(R.id.btnBarista)
        mButtonEditCheckin = (ImageButton) itemView.findViewById(R.id.edit_checkin)
        shareCheckin = (Button) itemView.findViewById(R.id.btnShare)
    }

    private setUserActions() {
        mButtonNote.onClickListener = {
            NoteDialog noteDialog = NoteDialog.newInstance(checkin.note)
            noteDialog.onNoteSubmit = { String note ->
                updateNoteInCheckin(note)
            }
            noteDialog.show(fragmentManager, "note_dialog")
        }
        mBarista.onClickListener = {
            Intent intent = BaristaActivity.newIntentWithContext(getContext())
            intent.putExtra("checkingId", mCheckinId)
            startActivity(intent)
        }

        mButtonEditCheckin.onClickListener = {
            Intent intent = CheckInActivity.newIntentWithContext(getContext())
            intent.putExtra(ACTION_CHECK_IN, 1)
            intent.putExtra(CURRENT_CHECK_IN_ID, checkin)
            startActivity(intent)
        }

        shareCheckin.onClickListener = {
            if (ShareDialog.canShow(ShareLinkContent.class) && checkin.s3_asset)
                sharePhotoContent()
            else
                Toast.makeText(context, R.string.message_add_photo, Toast.LENGTH_SHORT).show()
        }
    }

    private void sharePhotoContent() {
        loadingDialog.show(getActivity().getSupportFragmentManager(), "Sharing dialog")
        Fluent.async {
            checkin.s3_asset.url_file.toURL().bytes
        } then { result ->
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(result as byte[]))
            loadingDialog.dismiss()

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .setCaption(checkin.note)
                    .build();

            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content)
        }
    }

    private void setCheckinInView(Checkin checkin) {
        mOrigin.text = checkin.origin
        mMethod.text = checkin.method
        mPrice.text = checkin.price ? "\$ ${checkin.price}" : ""
        mNote.text = checkin.note ? " \"${checkin.note}\" " : ""
        mBaristaName.text = checkin?.baristum.id ? "Preparado por ${checkin?.baristum?.name}" : ""
        mDateCreated.text = checkin.created_at.format("HH:mm - dd/MM/yyyy")

        def url_image = checkin?.s3_asset?.url_file
        if (url_image)
            mImageUtil1.setPhotoImageView(getContext(), url_image, showImage)

    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            checkin = response.body()
            setCheckinInView(checkin)
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error " + t.message) }
    }

    void updateNoteInCheckin(String currentNote) {
        CheckinCommand checkinCommand = new CheckinCommand(note: currentNote)
        mCheckinManager.saveNote(checkin.id, checkinCommand, onSuccess(), onError())
    }
}
package com.makingdevs.mybarista.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.facebook.share.widget.ShareDialog
import com.makingdevs.mybarista.R
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
import com.makingdevs.mybarista.view.NoteDialog
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCheckinFragment extends Fragment implements OnActivityResultGallery {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "ShowCheckInFragment"
    private static final String CURRENT_CHECK_IN = "check_in"
    private static final String CHECK_IN_ID = "check_in_id"
    private static final String ACTION_CHECK_IN = "action_check_in"
    private static final String CURRENT_CIRCLE_FLAVOR = "circle_flavor"
    private static final int EDIT_REQUEST_CODE = 1
    private static final int BARIST_REQUEST_CODE = 2


    ImageUtil mImageUtil1 = new ImageUtil()

    TextView stateName
    TextView mMethod
    TextView mPrice
    TextView mNote
    TextView mDateCreated
    TextView mBaristaName
    View itemView
    ImageButton mButtonNote
    User currentUser
    ImageButton mBarista
    String mCheckinId
    Checkin checkin
    ImageButton mButtonEditCheckin
    ViewStub userActionsView
    Button shareCheckin
    ShareDialog shareDialog

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
        //Current Check In
        checkin = getActivity().intent.extras.getSerializable(CURRENT_CHECK_IN) as Checkin
        mCheckinId = getActivity().getIntent().getExtras().getString(CHECK_IN_ID)
        if (!mCheckinId)
            throw new IllegalArgumentException("No arguments $mCheckinId")

        bindingViews()
        validateCheckInAuthor()
        setCheckInInView(checkin)
        setUserActions()

        itemView
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case EDIT_REQUEST_CODE:
                    checkin = data.getExtras().getSerializable(CURRENT_CHECK_IN) as Checkin
                    setCheckInInView(checkin)
                    break
                case BARIST_REQUEST_CODE:
                    checkin = data.getExtras().getSerializable(CURRENT_CHECK_IN) as Checkin
                    setCheckInInView(checkin)
                    break
            }
        }
    }

    private void bindingViews() {
        userActionsView = (ViewStub) itemView.findViewById(R.id.stub_bottons)
        userActionsView.inflate()
        stateName = (TextView) itemView.findViewById(R.id.state_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        mBaristaName = (TextView) itemView.findViewById(R.id.barista_name_data)
        showImage = (ImageView) itemView.findViewById(R.id.show_photo_checkin)
        mDateCreated = (TextView) itemView.findViewById(R.id.label_created)
        mButtonNote = (ImageButton) itemView.findViewById(R.id.btnNote)
        mBarista = (ImageButton) itemView.findViewById(R.id.btnBarista)
        mButtonEditCheckin = (ImageButton) itemView.findViewById(R.id.edit_checkin)
        shareCheckin = (Button) itemView.findViewById(R.id.btnShare)
    }

    private void validateCheckInAuthor() {
        if (checkin.author != currentUser.username) {
            mButtonEditCheckin.setVisibility(View.GONE)
            mBarista.setVisibility(View.GONE)
            mButtonNote.setVisibility(View.GONE)
            shareCheckin.setVisibility(View.GONE)
        }
    }

    private void setCheckInInView(Checkin checkin) {
        stateName.text = checkin.state
        mMethod.text = checkin.method
        mPrice.text = checkin.price ? "\$ ${checkin.price}" : ""
        mNote.text = checkin.note ? " \"${checkin.note}\" " : ""
        mBaristaName.text = checkin?.baristum?.id ? "Preparado por ${checkin?.baristum?.name}" : ""
        mDateCreated.text = checkin.created_at.format("HH:mm - dd/MM/yyyy")

        def url_image = checkin?.s3_asset?.url_file
        if (url_image)
            mImageUtil1.setPhotoImageView(getContext(), url_image, showImage)

    }

    private setUserActions() {
        mButtonNote.onClickListener = {
            NoteDialog noteDialog = NoteDialog.newInstance(checkin.note)
            noteDialog.onNoteSubmit = { String note ->
                updateNoteInCheckIn(note)
            }
            noteDialog.show(fragmentManager, "note_dialog")
        }
        mBarista.onClickListener = {
            Intent intent = BaristaActivity.newIntentWithContext(getContext())
            intent.putExtra(CHECK_IN_ID, mCheckinId)
            startActivityForResult(intent, 2)
        }

        mButtonEditCheckin.onClickListener = {
            Intent intent = CheckInActivity.newIntentWithContext(getContext())
            intent.putExtra(ACTION_CHECK_IN, 1)
            intent.putExtra(CURRENT_CHECK_IN, checkin)
            startActivityForResult(intent, 1)
        }

        shareCheckin.onClickListener = {
            if (ShareDialog.canShow(ShareLinkContent.class) && checkin.s3_asset)
                sharePhotoContent()
            else
                Toast.makeText(context, R.string.message_add_photo, Toast.LENGTH_SHORT).show()
        }
    }

    void updateNoteInCheckIn(String currentNote) {
        CheckinCommand checkinCommand = new CheckinCommand(note: currentNote)
        mCheckinManager.saveNote(checkin.id, checkinCommand, onSuccess(), onError())
    }

    private void sharePhotoContent() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(String.format(getString(R.string.url_barista_profile), checkin.author)))
                .setContentTitle(String.format(getString(R.string.title_barista_photo), checkin.author))
                .setImageUrl(Uri.parse(checkin.s3_asset.url_file))
                .setContentDescription(checkin.note)
                .build()
        shareDialog.show(content)
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            checkin = response.body()
            setCheckInInView(checkin)
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t -> Log.d(TAG, t.message) }
    }
}
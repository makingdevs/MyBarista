package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

    private static final String TAG = "ShowCheckinFragment"
    private static final String CURRENT_CHECK_IN = "check_in"
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

    ShowCheckinFragment() { super() }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.fragment_show_chek_in, container, false)

        //Current User
        currentUser = mSessionManager.getUserSession(getContext())

        //Current Checkin
        mCheckinId = getActivity().getIntent().getExtras().getString("checkin_id")
        if (!mCheckinId)
            throw new IllegalArgumentException("No arguments $mCheckinId")


        ViewStub stub = (ViewStub) itemView.findViewById(R.id.stub_bottons)
        stub.inflate()

        findingElements()
        bindingElements()

        itemView
    }


    @Override
    void onResume() {
        super.onResume()
        mCheckinManager.show(mCheckinId, onSuccess(), onError())
    }

    private void findingElements() {
        mBarista = (Button) itemView.findViewById(R.id.btnBarista)
        mOrigin = (TextView) itemView.findViewById(R.id.origin_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        mBaristaName = (TextView) itemView.findViewById(R.id.barista_name_data)
        mButtonNote = (ImageButton) itemView.findViewById(R.id.btnNote)
        showImage = (ImageView) itemView.findViewById(R.id.show_photo_checkin)
        mDateCreated = (TextView) itemView.findViewById(R.id.label_created)
        mButtonEditCheckin = (ImageButton) itemView.findViewById(R.id.edit_checkin)
    }

    private bindingElements() {
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
            intent.putExtra(CURRENT_CHECK_IN, checkin)
            startActivity(intent)
        }

    }

    void updateNoteInCheckin(String currentNote) {
        CheckinCommand checkinCommand = new CheckinCommand(note: currentNote)
        mCheckinManager.saveNote(checkin.id, checkinCommand, onSuccess(), onError())
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
            validateCheckinAuthor()
            setCheckinInView(checkin)
        }
    }

    private void validateCheckinAuthor(){
        if(checkin.author != currentUser.username){
            mButtonEditCheckin.setVisibility(View.GONE)
            mButtonNote.setVisibility(View.GONE)
            mBarista.setVisibility(View.GONE)
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error " + t.message) }
    }
}
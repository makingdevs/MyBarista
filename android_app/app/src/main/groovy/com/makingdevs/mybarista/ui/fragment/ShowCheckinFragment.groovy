package com.makingdevs.mybarista.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
import com.makingdevs.mybarista.common.RequestPermissionAndroid
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.BaristaActivity
import com.makingdevs.mybarista.ui.activity.CircleFlavorActivity
import com.makingdevs.mybarista.view.NoteDialog
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCheckinFragment extends Fragment implements OnActivityResultGallery {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    S3assetManager mS3Manager = S3assetManagerImpl.instance

    private static final String TAG = "ShowCheckinFragment"
    private static String ID_CHECKIN

    ImageUtil mImageUtil1 = new ImageUtil()

    TextView mOrigin
    TextView mMethod
    TextView mPrice
    TextView mNote
    TextView mDateCreated
    TextView mBaristaName
    Button mButtonCircleFlavor
    View itemView
    ImageButton mButtonNote
    User currentUser
    Button mBarista

    String mCheckinId
    Checkin checkin
    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()

    ShowCheckinFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        mCheckinId = getActivity().getIntent().getExtras().getString("checkin_id")
        if (!mCheckinId)
            throw new IllegalArgumentException("No arguments $mCheckinId")
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.fragment_show_chek_in, container, false)
        findingElements()
        Log.d(TAG, mCheckinId.toString())
        currentUser = mSessionManager.getUserSession(getContext())
        mCheckinManager.show(mCheckinId, onSuccess(), onError())
        itemView
    }

    private void findingElements() {
        mButtonCircleFlavor = (Button) itemView.findViewById(R.id.btnCircle_flavor)
        mBarista = (Button) itemView.findViewById(R.id.btnBarista)
        mOrigin = (TextView) itemView.findViewById(R.id.origin_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        mBaristaName = (TextView) itemView.findViewById(R.id.barista_name_data)
        mButtonNote = (ImageButton) itemView.findViewById(R.id.btnNote)
        showImage = (ImageView) itemView.findViewById(R.id.show_photo_checkin)
        mDateCreated = (TextView) itemView.findViewById(R.id.label_created)
    }

    private void setCheckinInView(Checkin checkin) {
        mOrigin.text = checkin.origin
        mMethod.text = checkin.method

        mPrice.text = checkin.price ? "\$ ${checkin.price}" : ""
        mNote.text = checkin.note ? """ "${checkin.note}" """ : ""
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
            if (checkin.author == currentUser.username)
                showElements()
        }
    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            if (response.body())
                mImageUtil1.setPhotoImageView(getContext(), response.body().url_file, showImage)
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error " + t.message) }
    }

    private bindingElements() {
        /*mButtonNote.onClickListener = {
            if (checkPermissionStorage()) {
                requestPermissionAndroid.checkPermission(getActivity(), "storage")
            } else {
                Intent intent = ShowGalleryActivity.newIntentWithContext(getContext())
                intent.putExtra("CHECKINID", checkin.id)
                intent.putExtra("USERID", currentUser.id)
                intent.putExtra("CONTAINER", "checkin")
                startActivityForResult(intent, 1)
            }
        }*/

        mButtonNote.onClickListener = {
            NoteDialog noteDialog = NoteDialog.newInstance("")
            noteDialog.show(fragmentManager, "note_dialog")
        }

        mBarista.onClickListener = {
            Intent intent = BaristaActivity.newIntentWithContext(getContext())
            intent.putExtra("checkingId", mCheckinId)
            startActivity(intent)
        }
        mButtonCircleFlavor.onClickListener = {
            Intent intent = CircleFlavorActivity.newIntentWithContext(getContext())
            intent.putExtra("checkingId", mCheckinId)
            startActivity(intent)
        }

    }

    private void showElements() {
        ViewStub stub = (ViewStub) itemView.findViewById(R.id.stub_bottons)
        stub.inflate()
        findingElements()
        bindingElements()
    }

    boolean checkPermissionStorage() {
        Boolean status
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            status = true
        }
        status
    }


}
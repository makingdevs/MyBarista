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
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.BaristaActivity
import com.makingdevs.mybarista.ui.activity.CircleFlavorActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCheckinFragment extends Fragment {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    UserManager mUserManager = UserManagerImpl.instance

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
    ImageButton mButtonCamera
    User currentUser
    ImageView photoCheckinImageView
    Button mBarista

    String mCheckinId
    Checkin checkin

    ShowCheckinFragment(String id) {
        Bundle args = new Bundle()
        args.putSerializable(ID_CHECKIN, id)
        this.arguments = args
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        if (!getArguments() || !getArguments()?.getSerializable(ID_CHECKIN))
            throw new IllegalArgumentException("No arguments $ID_CHECKIN")
        mCheckinId = getArguments()?.getSerializable(ID_CHECKIN)
    }


    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.fragment_show_chek_in, container, false)
        findingElements()
        currentUser = mSessionManager.getUserSession(getContext())
        mUserManager.getPhoto(mCheckinId, onSuccessPhoto(), onError())
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
        mButtonCamera = (ImageButton) itemView.findViewById(R.id.button_camera)
        photoCheckinImageView = (ImageView) itemView.findViewById(R.id.show_photo_checkin)
        mDateCreated  = (TextView) itemView.findViewById(R.id.label_created)
    }

    private void setCheckinInView(Checkin checkin) {
        mOrigin.text = checkin.origin
        mMethod.text = checkin.method
        mPrice.text = checkin.price ? "\$ ${checkin.price}" : ""
        mNote.text = checkin.note ? """ "${checkin.note}" """ : ""
        mBaristaName.text = checkin?.baristum ? "Preparado por ${checkin?.baristum?.name}" : ""
        mDateCreated.text = checkin.created_at.format("HH:mm - dd/MM/yyyy")
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
                mImageUtil1.setPhotoImageView(getContext(), response.body().url_file, photoCheckinImageView)
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error " + t.message) }
    }

    private bindingElements() {
        mButtonCamera.onClickListener = {
            Fragment cameraFragment = new CameraFragment()
            cameraFragment.setSuccessActionOnPhoto { File photo ->
                mUserManager.upload(new UploadCommand(idCheckin: checkin.id,idUser:currentUser.id,pathFile: photo.getPath()),onSuccessPhoto(),onError())
            }
            cameraFragment.setErrorActionOnPhoto {
                Toast.makeText(getContext(), "Error al caputar la foto", Toast.LENGTH_SHORT).show()
            }
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bottomEmptyFragment, cameraFragment)
                    .addToBackStack(null).commit()

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

}
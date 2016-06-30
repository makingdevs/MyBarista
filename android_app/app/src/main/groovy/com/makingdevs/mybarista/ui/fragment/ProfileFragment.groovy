package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.LoginActivity
import retrofit2.Call
import retrofit2.Response

class ProfileFragment extends Fragment{

    ProfileFragment(){}
    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    ImageUtil mImageUtil1 = new ImageUtil()
    User currentUser

    private static final String TAG = "ProfileFragment"
    private EditText nameProfileEditText
    private EditText lastNameProfileEditText
    private TextView usernameProfile
    private Button checkinsCount
    private Button mSaveProfile
    TextView mCloseSession
    ImageView mImageViewCamera

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
    }

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_profile, container, false)
        currentUser = mSessionManager.getUserSession(getContext())
        nameProfileEditText = (EditText) root.findViewById(R.id.inputNameProfile)
        lastNameProfileEditText = (EditText) root.findViewById(R.id.inputLastNameProfile)
        usernameProfile = (TextView) root.findViewById(R.id.usernameProfile)
        usernameProfile.text = currentUser.username
        checkinsCount = (Button) root.findViewById(R.id.checkinsList)
        mSaveProfile = (Button) root.findViewById(R.id.save_profile)
        mCloseSession = (TextView) root.findViewById(R.id.close_session)
        mImageViewCamera = (ImageView) root.findViewById(R.id.photo_profile_user)
        //mImageUtil1.setPhotoImageView(getContext(),"http://mybarista.com.s3.amazonaws.com/coffee.jpg", mImageViewCamera)
        mSaveProfile.onClickListener = {
            updateInfoUserProfile()
        }
        mCloseSession.onClickListener = {
            mSessionManager.setLogout(getContext())
            Intent intent = LoginActivity.newIntentWithContext(getContext())
            startActivity(intent)
            getActivity().finish()
        }
        mImageViewCamera.onClickListener = {
            Fragment cameraFragment = new CameraFragment()
            cameraFragment.setSuccessActionOnPhoto { File photo ->
                mS3Manager.uploadPhotoUser(new UploadCommand(idUser: currentUser.id , pathFile: photo.getPath()), onSuccessPhoto(), onErrorPhoto())
            }
            cameraFragment.setErrorActionOnPhoto {
                Toast.makeText(getContext(), "Error al caputar la foto", Toast.LENGTH_SHORT).show()
            }
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, cameraFragment)
                    .addToBackStack(null).commit()
        }
        loadData()
        root
    }

    private void loadData(){
        mUserManager.getUser(currentUser.id,onSuccessUser(),onError())
    }

    private User updateInfoUserProfile() {
        String name = nameProfileEditText.text
        String lastName = lastNameProfileEditText.text
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(name:name,lastName:lastName,id:currentUser.id)
        sendUpdateUserProfile(updateUserCommand)

    }

    private void sendUpdateUserProfile(UpdateUserCommand updateUserCommand) {
        mUserManager.update(updateUserCommand, onSuccess(),onError())
    }


    private Closure onError() {
        { Call<UserProfile> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }

    private Closure onSuccess() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            Log.d(TAG,"Respueta:"+response.code())
            if(response.code() == 200){
                Toast.makeText(getContext(), "Datos Guardados Exitosamente", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Closure onSuccessUser() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            nameProfileEditText.text = response.body().name
            lastNameProfileEditText.text = response.body().lastName
            checkinsCount.text = "${response.body().checkins_count.toString()}\n Checkins"
        }
    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            println("FOTO... "+response?.body()?.dump().toString())
            /*Bundle bundle = new Bundle()
            bundle.putString("S3ASSET", response?.body()?.id?.toString())*/
            ProfileFragment profileFragment = new ProfileFragment()
            //profileFragment.setArguments(bundle)
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, profileFragment)
                    .addToBackStack(null).commit()
        }
    }

    private Closure onErrorPhoto() {
        { Call<Checkin> call, Throwable t -> Log.d("Error ${t.message}") }
    }
}
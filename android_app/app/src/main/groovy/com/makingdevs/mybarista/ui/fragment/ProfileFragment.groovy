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
import android.widget.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.OnActivityResultGallery
import com.makingdevs.mybarista.common.RequestPermissionAndroid
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.LoginActivity
import com.makingdevs.mybarista.ui.activity.ShowGalleryActivity
import retrofit2.Call
import retrofit2.Response

class ProfileFragment extends Fragment implements OnActivityResultGallery {

    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    ImageUtil mImageUtil1 = new ImageUtil()
    User currentUser
    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()

    private static final String TAG = "ProfileFragment"
    private EditText nameProfileEditText
    private EditText lastNameProfileEditText
    private TextView usernameProfile
    private Button checkinsCount
    private Button mSaveProfile
    TextView mCloseSession
    ImageView mImageViewCamera
    private UserProfile userProfile


    ProfileFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        userProfile = new UserProfile()
    }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        showImage = (ImageView) root.findViewById(R.id.photo_current_user)
        mSaveProfile.onClickListener = {
            updateInfoUserProfile()
        }
        mCloseSession.onClickListener = {
            LoginManager.getInstance().logOut()
            mSessionManager.setLogout(getContext())
            showLoginActivity()
            getActivity().finish()
        }
        mImageViewCamera.onClickListener = {
            if (checkPermissionStorage()) {
                requestPermissionAndroid.checkPermission(getActivity(), "storage")
            } else {
                Intent intent = ShowGalleryActivity.newIntentWithContext(getContext())
                intent.putExtra("USERID", currentUser.id)
                intent.putExtra("CONTAINER", "profile")
                startActivityForResult(intent, 1)
            }
        }
        loadData()
        root
    }

    private void showLoginActivity() {
        Intent intent = LoginActivity.newIntentWithContext(getContext())
        startActivity(intent)
    }

    private void loadData() {
        mUserManager.getUser(currentUser.id, onSuccessUser(), onError())
    }

    private User updateInfoUserProfile() {
        String name = nameProfileEditText.text
        String lastName = lastNameProfileEditText.text
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(name: name, lastName: lastName, id: currentUser.id)
        sendUpdateUserProfile(updateUserCommand)

    }

    private void sendUpdateUserProfile(UpdateUserCommand updateUserCommand) {
        mUserManager.update(updateUserCommand, onSuccess(), onError())
    }


    private Closure onError() {
        { Call<UserProfile> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }

    private Closure onSuccess() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            Log.d(TAG, "Respueta:" + response.code())
            if (response.code() == 200) {
                Toast.makeText(getContext(), "Datos Guardados Exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Closure onSuccessUser() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            userProfile = response.body()
            setUserProfileData(userProfile)
        }
    }

    private void setUserProfileData(UserProfile profile) {
        nameProfileEditText.text = profile.name
        lastNameProfileEditText.text = profile.lastName
        checkinsCount.text = "${userProfile.checkins_count.toString()}\n Checkins"
        String urlFile = profile?.s3_asset?.url_file
        if (urlFile) {
            mImageUtil1.setPhotoImageView(getContext(), urlFile, mImageViewCamera)
        }
    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            changeFragment(new ProfileFragment())
        }
    }

    private Closure onErrorPhoto() {
        { Call<Checkin> call, Throwable t ->
            Log.d("Error ${t.message}")
            changeFragment(new ProfileFragment())
        }
    }

    void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    boolean checkPermissionStorage() {
        Boolean status
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            status = true
        }
        status
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == activity.RESULT_OK) {
                mImageUtil1.setPhotoImageView(getContext(), data.getStringExtra("PATH_PHOTO"), mImageViewCamera)
            }
        }
    }
}
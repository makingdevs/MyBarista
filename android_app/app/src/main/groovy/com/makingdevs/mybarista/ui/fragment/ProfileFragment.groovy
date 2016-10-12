package com.makingdevs.mybarista.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.*
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.LoginActivity
import com.makingdevs.mybarista.ui.activity.ShowGalleryActivity
import groovy.transform.CompileStatic
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class ProfileFragment extends Fragment implements OnActivityResultGallery {

    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    S3assetManager s3assetManager = S3assetManagerImpl.instance
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
    private CamaraUtil camaraUtil
    private Button mWebProfile

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
        camaraUtil = new CamaraUtil()
        nameProfileEditText = (EditText) root.findViewById(R.id.inputNameProfile)
        lastNameProfileEditText = (EditText) root.findViewById(R.id.inputLastNameProfile)
        usernameProfile = (TextView) root.findViewById(R.id.usernameProfile)
        usernameProfile.text = currentUser.username
        checkinsCount = (Button) root.findViewById(R.id.checkinsList)
        mSaveProfile = (Button) root.findViewById(R.id.save_profile)
        mCloseSession = (TextView) root.findViewById(R.id.close_session)
        mImageViewCamera = (ImageView) root.findViewById(R.id.photo_profile_user)
        showImage = (ImageView) root.findViewById(R.id.photo_current_user)
        mWebProfile = (Button) root.findViewById(R.id.webProfile)
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
        mWebProfile.onClickListener = {
            showWebProfileTab()
        }
        loadData()
        root
    }

    private void showWebProfileTab() {
        Uri webProfileUrl = Uri.parse(String.format(getString(R.string.url_barista_profile, currentUser.username)))
        CustomTabsIntent profileTabIntent = new CustomTabsIntent.Builder().build();
        CustomTabActivityHelper.openCustomTab(activity, profileTabIntent, webProfileUrl,
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                    }
                });
    }

    private void showLoginActivity() {
        Intent intent = LoginActivity.newIntentWithContext(getContext())
        startActivity(intent)
    }

    private void loadData() {
        mUserManager.getUser(currentUser.id, onSuccessUser(), onError())
    }

    private void updateInfoUserProfile() {
        String name = nameProfileEditText.text
        String lastName = lastNameProfileEditText.text
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(name: name, lastName: lastName, id: currentUser.id)
        sendUpdateUserProfile(updateUserCommand)
    }

    private void sendUpdateUserProfile(UpdateUserCommand updateUserCommand) {
        mUserManager.update(updateUserCommand, onSuccess(), onError())
    }


    static Closure onError() {
        { Call<UserProfile> call, Throwable t -> Log.d(TAG, t.message) }
    }

    private Closure onSuccess() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            if (response.code() == 200) {
                Toast.makeText(getContext(), R.string.message_saved_data, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Closure onSuccessUser() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            userProfile = response.body()
            setUserProfileData(userProfile)

            if (userProfile.s3_asset == null && AccessToken.getCurrentAccessToken() != null)
                getFacebookPhotoProfile()
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

    /**
     * This piece of code is temporal since I do not know yet how to pass arguments from LoginFragment to Principal Activity
     */
    void getFacebookPhotoProfile() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject()

                try {
                    if (json != null) {
                        String facebookId = json.getString(getString(R.string.request_fb_id))
                        setFacebookProfilePhoto(facebookId)
                    }
                } catch (JSONException e) {
                    e.printStackTrace()
                }
            }
        })
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void setFacebookProfilePhoto(String facebookId) {
        String photoUrl = String.format(getString(R.string.url_facebook_photo), facebookId)
        String userId = currentUser.id.toString()

        Fluent.async {
            photoUrl.toURL().bytes
        } then { result ->
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(result as byte[]))
            String bitmapPath = camaraUtil.saveBitmapToFile(bitmap, "${facebookId}.png").path
            s3assetManager.uploadPhotoUser(new UploadCommand(idUser: userId, pathFile: bitmapPath), onSuccessPhoto(), onErrorPhoto())
        }
    }

    static Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            // Our Code
        }
    }

    static Closure onErrorPhoto() {
        { Call<Checkin> call, Throwable t ->
            // Our Code
        }
    }

    boolean checkPermissionStorage() {
        Boolean status = false
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
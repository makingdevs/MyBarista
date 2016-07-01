package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.ListBrewByUserActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class ProfilePublicFragment extends Fragment{

    UserManager mUserManager = UserManagerImpl.instance

    private static final String TAG = "ProfileFragment"
    private static String USER_ID
    private TextView fullNameEditText
    private TextView usernameProfile
    private Button checkinsCount
    String userId
    ImageView mImageViewProfilePublic
    ImageUtil mImageUtil1 = new ImageUtil()

    ProfilePublicFragment(String id){
        Bundle args = new Bundle()
        args.putSerializable(USER_ID, id)
        this.arguments = args
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        if(!getArguments() || !getArguments()?.getSerializable(USER_ID))
            throw new IllegalArgumentException("No arguments $USER_ID")
        userId = getArguments()?.getSerializable(USER_ID)
    }

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_profile_public, container, false)
        fullNameEditText = (TextView) root.findViewById(R.id.full_name)
        usernameProfile = (TextView) root.findViewById(R.id.usernameProfile)
        checkinsCount = (Button) root.findViewById(R.id.checkinsList)
        mImageViewProfilePublic = (ImageView) root.findViewById(R.id.photo_public_profile)

        loadData()
        root
    }

    private void loadData(){
        mUserManager.getUser(userId,onSuccessUser(),onError())
    }

    private Closure onError() {
        { Call<UserProfile> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }


    private Closure onSuccessUser() {
        { Call<UserProfile> call, Response<UserProfile> response ->
            fullNameEditText.text = "${response.body().name ?: ""} ${response.body().lastName ?: ""}"
            checkinsCount.text = "${response.body().checkins_count.toString()}\n Checkins"
            usernameProfile.text = response.body().username
            String url_image = response?.body()?.s3_asset?.url_file
            if (url_image){
                mImageUtil1.setPhotoImageView(getContext(),url_image, mImageViewProfilePublic)
            }

            checkinsCount.onClickListener = {
                Intent intent = ListBrewByUserActivity.newIntentWithContext(getContext(),response.body().username)
                startActivity(intent)
            }
        }
    }
}
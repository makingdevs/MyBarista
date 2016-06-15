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
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.ListBrewActivity
import com.makingdevs.mybarista.ui.activity.ListBrewByUserActivity
import retrofit2.Call
import retrofit2.Response

class ProfilePublicFragment extends Fragment{

    UserManager mUserManager = UserManagerImpl.instance

    private static final String TAG = "ProfileFragment"
    private static String USER_ID
    private TextView nameProfileEditText
    private TextView lastNameProfileEditText
    private TextView usernameProfile
    private Button checkinsCount
    String userId

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
        nameProfileEditText = (TextView) root.findViewById(R.id.inputNameProfile)
        lastNameProfileEditText = (TextView) root.findViewById(R.id.inputLastNameProfile)
        usernameProfile = (TextView) root.findViewById(R.id.usernameProfile)
        //usernameProfile.text = currentUser.username
        checkinsCount = (Button) root.findViewById(R.id.checkinsList)
        checkinsCount.onClickListener = {
            Intent intent = ListBrewByUserActivity.newIntentWithContext(getContext())
            startActivity(intent)
        }
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
            nameProfileEditText.text = response.body().name
            lastNameProfileEditText.text = response.body().lastName
            checkinsCount.text = "${response.body().checkins_count.toString()}\n Checkins"
        }
    }
}
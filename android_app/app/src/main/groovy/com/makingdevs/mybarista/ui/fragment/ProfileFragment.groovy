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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.ListBrewActivity
import retrofit2.Call
import retrofit2.Response


class ProfileFragment extends Fragment{

    ProfileFragment(){}
    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "ProfileFragment"
    private ImageButton mButtonProfileConfirm
    private ImageButton mButtonProfileCancel
    private EditText nameProfileEditText
    private EditText lastNameProfileEditText
    private TextView usernameProfile
    private Button checkinsCount
    User currentUser

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_profile, container, false)
        currentUser = mSessionManager.getUserSession(getContext())
        mButtonProfileConfirm = (ImageButton) root.findViewById(R.id.buttonProfileConfirm)
        mButtonProfileCancel = (ImageButton) root.findViewById(R.id.buttonProfileCancel)
        nameProfileEditText = (EditText) root.findViewById(R.id.inputNameProfile)
        lastNameProfileEditText = (EditText) root.findViewById(R.id.inputLastNameProfile)
        usernameProfile = (TextView) root.findViewById(R.id.usernameProfile)
        usernameProfile.text = currentUser.username
        checkinsCount = (Button) root.findViewById(R.id.checkinsList)
        checkinsCount.onClickListener = {
            //some
        }
        mButtonProfileConfirm.onClickListener = {
            updateInfoUserProfile()
        }
        mButtonProfileCancel.onClickListener = {
            Intent intent = ListBrewActivity.newIntentWithContext(getContext())
            startActivity(intent)
            getActivity().finish()
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
}
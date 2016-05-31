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
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.CheckinActivity
import com.makingdevs.mybarista.ui.activity.ListBrewActivity
import com.makingdevs.mybarista.ui.activity.LoginActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class RegistrationFragment extends Fragment{

    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "RegistrationFragment"
    private EditText userNameEditText
    private EditText passwordEditText
    private EditText confirmPasswordEditText
    private Button mButtonRegistration

    RegistrationFragment(){}

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_registration, container, false)
        userNameEditText = (EditText) root.findViewById(R.id.username)
        passwordEditText = (EditText) root.findViewById(R.id.password)
        confirmPasswordEditText = (EditText) root.findViewById(R.id.confirm_password)
        mButtonRegistration = (Button) root.findViewById(R.id.btnRegistration)
        mButtonRegistration.onClickListener = {  getFormRegistration() }
        root
    }

    private void getFormRegistration(){
        String username = userNameEditText.text
        String password = passwordEditText.text
        String confirmPassword = confirmPasswordEditText.text
        RegistrationCommand registrationCommand = new RegistrationCommand(username:username,password:password,confirmPassword:confirmPassword)
        validateRegistration(registrationCommand)
    }

    private void validateRegistration(RegistrationCommand registrationCommand){
        if(registrationCommand.validateCommand())
            createNewUser(registrationCommand)
        else
            cleanForm()
    }

    private void createNewUser(RegistrationCommand registrationCommand) {
        mUserManager.save(registrationCommand,onSuccess(),OnError())
    }

    private Closure OnError() {
        { Call<User> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }

    private Closure onSuccess() {
        { Call<User> call, Response<User> response ->
            Log.d(TAG,"Respueta:"+response.code())
            if(response.code() == 201){
                mSessionManager.setUserSession(response.body(),getContext())
                Intent intent = ListBrewActivity.newIntentWithContext(getContext())
                startActivity(intent)
                getActivity().finish()
            }
            else {
                Toast.makeText(getContext(), R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void cleanForm(){
        Toast.makeText(getContext(), R.string.toastRegistrationFail, Toast.LENGTH_SHORT).show()
        passwordEditText.text = ""
        confirmPasswordEditText.text = ""
    }

}
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
import com.google.gson.Gson
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
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
    EditText mEmailEditText

    RegistrationFragment() { super() }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_registration, container, false)
        userNameEditText = (EditText) root.findViewById(R.id.username)
        passwordEditText = (EditText) root.findViewById(R.id.password)
        confirmPasswordEditText = (EditText) root.findViewById(R.id.confirm_password)
        mButtonRegistration = (Button) root.findViewById(R.id.btnRegistration)
        mEmailEditText = (EditText) root.findViewById(R.id.email)
        mButtonRegistration.onClickListener = {  getFormRegistration() }
        root
    }

    private void getFormRegistration(){
        String username = userNameEditText.text
        String password = passwordEditText.text
        String confirmPassword = confirmPasswordEditText.text
        String email = mEmailEditText.text
        RegistrationCommand registrationCommand = new RegistrationCommand(username:username,password:password,confirmPassword:confirmPassword,email:email)
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
            if(response.isSuccessful()){
                mSessionManager.setUserSession(response.body(),getContext())
                Intent intent = PrincipalActivity.newIntentWithContext(getContext())
                startActivity(intent)
                getActivity().finish()
            }else {
                Map<String, List<String>> errorMessages = new Gson().fromJson(response.errorBody().string(), Map)
                List message = errorMessages.collect{k,v ->
                    " El ${k} ${v[0]}"
                }
                Toast.makeText(getContext(),message.join(","), Toast.LENGTH_LONG).show()
            }

        }
    }

    private void cleanForm(){
        Toast.makeText(getContext(), R.string.toastRegistrationFail, Toast.LENGTH_SHORT).show()
        passwordEditText.text = ""
        confirmPasswordEditText.text = ""
    }

}
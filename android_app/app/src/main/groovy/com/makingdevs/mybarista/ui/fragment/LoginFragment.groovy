package com.makingdevs.mybarista.ui.fragment

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
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class LoginFragment extends Fragment{

    UserManager mUserManager = UserManagerImpl.instance

    private static final String TAG = "LoginFragment"
    private EditText userNameEditText
    private EditText passwordEditText
    private Button mButtonLogin

    LoginFragment(){}

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_login, container, false)
        userNameEditText = (EditText) root.findViewById(R.id.username_login)
        passwordEditText = (EditText) root.findViewById(R.id.password_login)
        mButtonLogin = (Button) root.findViewById(R.id.btnLogin)
        mButtonLogin.onClickListener = {
                getFormLogin()
            }
        root
    }

    private void getFormLogin(){
        LoginCommand loginCommand = new LoginCommand(username:userNameEditText.text.toString(),password:passwordEditText.text.toString())
        validateForm(loginCommand)
    }

    private void validateForm(LoginCommand loginCommand){
        if(loginCommand.validateCommand())
            mUserManager.login(loginCommand,onSuccess(),OnError())
        else
            cleanForm()

    }

    private void cleanForm(){
        passwordEditText.text = ""
        Toast.makeText(getContext(), R.string.toastLoginFail, Toast.LENGTH_SHORT).show()
    }

    private Closure OnError() {
        { Call<User> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }

    private Closure onSuccess() {
        { Call<User> call, Response<User> response ->
            Log.d(TAG,"Respueta:"+response.code())
            if(response.code() == 200){
                Toast.makeText(getContext(), R.string.toastLoginSuccess, Toast.LENGTH_SHORT).show()
                //Intent intent = LoginActivity.newIntentWithContext(getContext())
                //startActivity(intent)
            }
            else {
                cleanForm()
            }

        }
    }
}
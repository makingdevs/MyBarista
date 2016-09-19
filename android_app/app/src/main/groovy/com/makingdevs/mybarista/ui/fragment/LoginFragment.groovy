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
import android.widget.TextView
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
import com.makingdevs.mybarista.ui.activity.RegistrationActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class LoginFragment extends Fragment implements FacebookCallback<LoginResult>{

    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "LoginFragment"
    private EditText userNameEditText
    private EditText passwordEditText
    private TextView messageToRegister
    private Button mButtonLogin
    private LoginButton buttonFacebookLogin
    private CallbackManager callbackManager
    LoginFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create()
    }

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        validateHasUserSession()
        View root = inflater.inflate(R.layout.fragment_login, container, false)
        userNameEditText = (EditText) root.findViewById(R.id.input_username)
        passwordEditText = (EditText) root.findViewById(R.id.input_password)
        messageToRegister = (TextView) root.findViewById(R.id.message_register)
        mButtonLogin = (Button) root.findViewById(R.id.btnLogin)
        mButtonLogin.onClickListener = { getFormLogin() }
        messageToRegister.onClickListener = {
            Intent intent = RegistrationActivity.newIntentWithContext(getContext())
            startActivity(intent)
        }
        buttonFacebookLogin = (LoginButton) root.findViewById(R.id.login_button)
        configFacebookLogin()
        root
    }

    private void configFacebookLogin () {
        List permissions = new ArrayList()
        permissions.add(getString(R.string.permission_fb_email))
        permissions.add(getString(R.string.permission_fb_birthday))

        buttonFacebookLogin.setReadPermissions(permissions)
        buttonFacebookLogin.setFragment(this)
        buttonFacebookLogin.registerCallback(callbackManager, this)
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private void validateHasUserSession(){
        if (mSessionManager.isUserSession(getContext())){
            showPrincipalActivity()
        }
    }

    private void getFormLogin(){
        LoginCommand loginCommand = new LoginCommand(username:userNameEditText.text.toString(),password:passwordEditText.text.toString())
        mUserManager.login(loginCommand, onLoginSuccess(), onLoginError())
    }

    private void cleanForm(){
        passwordEditText.text = ""
        Toast.makeText(getContext(), R.string.toastLoginFail, Toast.LENGTH_SHORT).show()
    }

    private Closure onLoginError() {
        { Call<User> call, Throwable t -> Log.d("ERRORZ", t.toString()) }
    }

    private Closure onLoginSuccess() {
        { Call<User> call, Response<User> response ->
            if(response.code() == 200){
                mSessionManager.setUserSession(response.body(),getContext())
                showPrincipalActivity()
            }
            else {
                cleanForm()
            }

        }
    }

    /**
     * Facebook Login
     * @param loginResult
     */

    @Override
    void onSuccess(LoginResult loginResult) {
        showPrincipalActivity()
    }

    @Override
    void onCancel() {
        Toast.makeText(context, R.string.message_fb_session_cancel, Toast.LENGTH_SHORT)
        .show()
    }

    @Override
    void onError(FacebookException error) {
        Toast.makeText(context, R.string.message_fb_session_error, Toast.LENGTH_SHORT)
        .show()
    }

    private void showPrincipalActivity() {
        Intent intent = PrincipalActivity.newIntentWithContext(getContext())
        startActivity(intent)
        getActivity().finish()
    }
}
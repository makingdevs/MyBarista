package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
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
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class LoginFragment extends Fragment implements FacebookCallback<LoginResult> {

    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "LoginFragment"
    private EditText userNameEditText
    private EditText passwordEditText
    private TextView messageToRegister
    private Button mButtonLogin
    private LoginButton buttonFacebookLogin
    private CallbackManager callbackManager
    String facebookId, email, first_name, last_name, birthday, token

    LoginFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create()
    }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    private void configFacebookLogin() {
        buttonFacebookLogin.setReadPermissions(Arrays.asList(getString(R.string.permission_fb_email), getString(R.string.permission_fb_birthday)))
        buttonFacebookLogin.setFragment(this)
        buttonFacebookLogin.registerCallback(callbackManager, this)
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private void validateHasUserSession() {
        if (mSessionManager.isUserSession(getContext())) {
            showPrincipalActivity()
        }
    }

    private void getFormLogin() {
        LoginCommand loginCommand = new LoginCommand(username: userNameEditText.text.toString(), password: passwordEditText.text.toString())
        mUserManager.login(loginCommand, onLoginSuccess(), onLoginError())
    }

    private void cleanForm() {
        passwordEditText.text = ""
        Toast.makeText(getContext(), R.string.toastLoginFail, Toast.LENGTH_SHORT).show()
    }

    private Closure onLoginError() {
        { Call<User> call, Throwable t ->
            LoginManager.instance.logOut()
        }
    }

    private Closure onLoginSuccess() {
        { Call<User> call, Response<User> response ->
            if (response.code() == 200) {
                mSessionManager.setUserSession(response.body(), getContext())
                showPrincipalActivity()
            } else {
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
        GraphRequest request = GraphRequest.newMeRequest(loginResult.accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                if (AccessToken.getCurrentAccessToken() != null)
                    getFacebookUserData(loginResult, graphResponse)
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, birthday");
        request.setParameters(parameters);
        request.executeAsync();

        LoginCommand loginCommand = new LoginCommand(email: email, token: token)
        mUserManager.login(loginCommand, onLoginSuccess(), onLoginError())
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

    private void getFacebookUserData(LoginResult loginResult, GraphResponse graphResponse) {
        JSONObject json = graphResponse.getJSONObject()

        try {
            if (json != null) {
                facebookId = json.getString(getString(R.string.request_fb_id))
                email = json.getString(getString(R.string.request_fb_email))
                token = loginResult.accessToken.token
                first_name = json.getString(getString(R.string.request_fb_first_name))
                last_name = json.getString(getString(R.string.request_fb_last_name))
                birthday = json.getString(getString(R.string.request_fb_birthday))
            }
        } catch (JSONException e) {
            e.printStackTrace()
        }
    }

    private void showPrincipalActivity() {
        Intent intent = PrincipalActivity.newIntentWithContext(getContext())
        startActivity(intent)
        getActivity().finish()
    }
}
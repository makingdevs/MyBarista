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
import groovy.transform.CompileStatic

@CompileStatic
class LoginFragment extends Fragment{

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
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                getFormLogin()
            }
        })
        root
    }

    private void getFormLogin(){
        Map loginData = [:]
        String username = userNameEditText.text
        String password = passwordEditText.text
        loginData << ["username":username,"password":password]
        validateForm(loginData)
    }

    private void validateForm(Map loginData){
        def pattern = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/
        if(loginData.username ==~ pattern){
            Toast.makeText(getContext(), R.string.toastLoginSuccess, Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(getContext(), R.string.toastLoginFail, Toast.LENGTH_SHORT).show()
            cleanForm()
        }
    }

    private void cleanForm(){
        passwordEditText.text = ""
    }
}
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
import com.makingdevs.mybarista.model.RegistrationCommand
import groovy.transform.CompileStatic

@CompileStatic
class RegistrationFragment extends Fragment{

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
        mButtonRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            void onClick(View v) {
                getFormRegistration()
            }
        })

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
        def pattern = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/
        if(registrationCommand.username ==~ pattern && registrationCommand.password == registrationCommand.confirmPassword && registrationCommand.username != registrationCommand.password){
            Toast.makeText(getContext(), "Username and Password success!", Toast.LENGTH_SHORT).show()
            //TODO Consumir servicio para registro en rails
        }
        else{
            Toast.makeText(getContext(), "Username or Password incorrect!", Toast.LENGTH_SHORT).show()
            cleanForm()
        }
    }

    private void cleanForm(){
        passwordEditText.text = ""
        confirmPasswordEditText.text = ""
    }

}
package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response


@CompileStatic
public class FormCheckinFragment extends Fragment {

    private static final String TAG = "FormCheckinFragment"
    private EditText originEditText
    private EditText priceEditText
    private EditText noteEditText
    private Spinner methodFieldSprinner
    private Button checkInButton
    private static Context contextView

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    FormCheckinFragment() {}

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_chek_in, container, false)
        originEditText = (EditText) root.findViewById(R.id.originField)
        priceEditText = (EditText) root.findViewById(R.id.priceField)
        noteEditText = (EditText) root.findViewById(R.id.noteField)
        methodFieldSprinner = (Spinner) root.findViewById(R.id.methodSpinner)
        checkInButton = (Button) root.findViewById(R.id.btnCheckIn);
        contextView = getActivity().getApplicationContext()
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCheckIn(getFormCheckIn())
            }
        });

        root
    }

    private CheckinCommand getFormCheckIn(){
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String note = noteEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        User currentUser =  mSessionManager.getUserSession(getContext())
        new CheckinCommand(method:method,note:note,origin:origin,price:price?.toString(),username:currentUser.username)
    }

    private void saveCheckIn(CheckinCommand checkin) {
        mCheckinManager.save(checkin, onSuccess(), onError())
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG, response.dump().toString())
            if (response.code() == 201) {
                Toast.makeText(contextView, R.string.toastCheckinSuccess, Toast.LENGTH_SHORT).show();
                cleanForm()
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
    }

    private void cleanForm(){
        originEditText.setText("")
        priceEditText.setText("")
        noteEditText.setText("")
    }

}
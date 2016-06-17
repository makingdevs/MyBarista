package com.makingdevs.mybarista.ui.fragment

import android.content.Context
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
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.command.BaristaCommand
import com.makingdevs.mybarista.service.BaristaManager
import com.makingdevs.mybarista.service.BaristaManagerImpl
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity
import groovy.transform.CompileStatic
import groovyjarjarantlr.collections.List
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class BaristaFragment extends Fragment {

    private static final String TAG = "BaristaFragment"
    private EditText mNameBarista
    private Button  mButtonCreateBarista
    private static Context contextView


    BaristaManager mBaristaManager = BaristaManagerImpl.instance



    View onCreateView(LayoutInflater inflater,
                       @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String id = getActivity().getIntent().getExtras().getString("checkingId")
        Log.d(TAG,id)
        View root = inflater.inflate(R.layout.fragment_new_barista,container, false)
        mNameBarista = (EditText) root.findViewById(R.id.name_barista_field)
        mButtonCreateBarista = (Button) root.findViewById(R.id.button_new_barista)
        mButtonCreateBarista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               saveBarista(getPropertiesOfBarista(), id)
            }
        })
        root

    }

    private BaristaCommand getPropertiesOfBarista() {
        String name = mNameBarista.getText().toString()
        new BaristaCommand(name:  name)
    }

    private void saveBarista(BaristaCommand command, String id) {
        mBaristaManager.save(command,id,onSuccess(), onError())
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 200) {
               showCheckin(response.body())
            } else {
                Log.d(TAG, "Errorrrrz")
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
    }

    private void showCheckin(Checkin checkin){
        Intent intent = ShowCheckinActivity.newIntentWithContext(getContext(),checkin.id, checkin.circle_flavor_id)
        intent.putExtra("circle_flavor_id",checkin.circle_flavor_id)
        startActivity(intent)
    }
}
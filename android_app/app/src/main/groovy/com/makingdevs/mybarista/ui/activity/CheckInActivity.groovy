package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class CheckInActivity extends AppCompatActivity {

    private static final String TAG = "CheckInActivity"
    private EditText originEditText
    private EditText priceEditText
    private EditText noteEditText
    private Spinner methodFieldSprinner
    private Button checkInButton
    private static Context contextView

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    private void initListenerCheckIn(){
        checkInButton = (Button) findViewById(R.id.btnCheckIn);
        checkInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Map<String,String> parameters = getFormCheckIn()
                saveCheckIn(parameters)
            }
        });
    }

    private Map<String,String> getFormCheckIn(){
        originEditText = (EditText) findViewById(R.id.originField)
        priceEditText = (EditText) findViewById(R.id.priceField)
        noteEditText = (EditText) findViewById(R.id.noteField)
        methodFieldSprinner = (Spinner) findViewById(R.id.methodSpinner)
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String note = noteEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        Map<String,String> parameters = new HashMap<String, String>()
        parameters << ["method":method,"note":note,"origin":origin,"price":price?.toString()]
    }

    private void saveCheckIn(Map<String,String> parameters) {
        mCheckinManager.save(parameters, onSuccess(), onError())
    }

    private Closure onSuccess(){
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,response.dump().toString())
            if (response.code() == 201){
                Toast.makeText(contextView,R.string.toastCheckinSuccess,Toast.LENGTH_SHORT).show();
                cleanForm()
            }else {
                Toast.makeText(contextView,R.string.toastCheckinFail,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onError(){
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView,R.string.toastCheckinFail,Toast.LENGTH_SHORT).show();
        }
    }

    private void cleanForm(){
        originEditText.setText("")
        priceEditText.setText("")
        noteEditText.setText("")
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)
        //TODO esto no sabemos si se puede hacer de una mejor manera
        contextView = this
        initListenerCheckIn()
    }

}





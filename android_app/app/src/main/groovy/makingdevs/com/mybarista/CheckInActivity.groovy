package makingdevs.com.mybarista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner
import android.widget.SpinnerAdapter
import groovy.transform.CompileStatic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@CompileStatic
public class CheckInActivity extends AppCompatActivity {

    private static final String TAG = "CheckInActivity";
    private Button checkInButton;

    private void initListenerCheckIn(){
        checkInButton = (Button) findViewById(R.id.btnCheckIn);
        checkInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getFormCheckIn();
                saveCheckIn();
            }
        });
    }

    private void getFormCheckIn(){
        final EditText originEditText = (EditText) findViewById(R.id.originField);
        final EditText priceEditText = (EditText) findViewById(R.id.priceField);
        final EditText noteEditText = (EditText) findViewById(R.id.noteField);
        String origin = originEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String note = noteEditText.getText().toString();

        final Spinner methodFieldSprinner = (Spinner) findViewById(R.id.methodSpinner);
        String method = methodFieldSprinner.getSelectedItem().toString();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        initListenerCheckIn();
    }

    private void saveCheckIn() {
        Log.d(TAG,"algo");
    }
}





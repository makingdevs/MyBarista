package makingdevs.com.mybarista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner
import groovy.transform.CompileStatic
import makingdevs.com.mybarista.model.Checkin
import makingdevs.com.mybarista.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@CompileStatic
public class CheckInActivity extends AppCompatActivity {

    private static final String TAG = "CheckInActivity";
    private Button checkInButton;

    private final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl('http://192.168.1.198:3000/')
            .build()


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
        final EditText originEditText = (EditText) findViewById(R.id.originField);
        final EditText priceEditText = (EditText) findViewById(R.id.priceField);
        final EditText noteEditText = (EditText) findViewById(R.id.noteField);
        String origin = originEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String note = noteEditText.getText().toString();
        final Spinner methodFieldSprinner = (Spinner) findViewById(R.id.methodSpinner);
        String method = methodFieldSprinner.getSelectedItem().toString();
        Map<String,String> parameters = new HashMap<String, String>()
        parameters << ["method":method,"note":note,"origin":origin,"price":price?.toString()]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        initListenerCheckIn();
    }

    private void saveCheckIn(Map<String,String> parameters) {
        ApiService owm = retrofit.create(ApiService)
        Call<Checkin> model = owm.createCheckinForm(parameters.method,parameters.note,parameters.origin,parameters.price)
        def callback = [
                onResponse :{Call<Checkin> call, Response<Checkin> response ->
                    Log.d(TAG,response.dump().toString())
                                Log.d(TAG, response.message())
                            },
                onFailure : {Call<Checkin> call, Throwable t -> Log.d(TAG, "el error",t) }
        ]
        model.enqueue(callback as Callback<Checkin>)
    }
}





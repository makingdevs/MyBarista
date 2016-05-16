package makingdevs.com.mybarista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends AppCompatActivity {

    private static final String TAG = "CheckInActivity";
    private Spinner methodCoffe;
    private Button checkInButton;

    private void loadMethodsCoffe(){
        methodCoffe = (Spinner) findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("Expresso");
        list.add("Americano");
        list.add("Goteo");
        list.add("Prensa");
        list.add("Sifón");
        list.add("Otro");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        methodCoffe.setAdapter(dataAdapter);
    }

    private void initListenerCheckIn(){
        checkInButton = (Button) findViewById(R.id.btnCheckIn);
        checkInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "huesos");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        loadMethodsCoffe();
        initListenerCheckIn();
    }
}

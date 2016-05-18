package makingdevs.com.mybarista;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar

import android.util.Log;
import android.view.View
import groovy.transform.CompileStatic
import makingdevs.com.mybarista.model.Checkin
import makingdevs.com.mybarista.service.ApiService
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;

@CompileStatic
public class MyCheckInsActivity extends AppCompatActivity {

    private static final String TAG = "MyCheckInsActivity"


    private final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl('http://192.168.1.198:3000/')
            .build()


    private void initListCheckins(){
        ApiService owm = retrofit.create(ApiService)
        Call<List<Checkin>> model = owm.getCheckins()
        def callback = [
                onResponse :{Call<List<Checkin>> call, Response<List<Checkin>> response ->
                    Log.d(TAG, response.body().toString())
                },
                onFailure : {Call<List<Checkin>> call, Throwable t -> Log.d(TAG, "el error") }
        ]
        model.enqueue(callback as Callback<List<Checkin>>)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_check_ins);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initListCheckins()
    }

}

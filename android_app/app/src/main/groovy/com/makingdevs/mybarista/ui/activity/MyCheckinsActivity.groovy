package com.makingdevs.mybarista.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.network.CheckinRestOperations
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@CompileStatic
public class MyCheckinsActivity extends AppCompatActivity {

    private static final String TAG = "MyCheckInsActivity"
    ListView listView

    private final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl('http://192.168.1.198:3000/')
            .build()

    private void createListView(Response<List<Checkin>> response){
        listView = (ListView) findViewById(R.id.listCheckinsView)
        ArrayAdapter<String> adapter
        String[] checkins = response.body().method

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,checkins)
            listView.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    @Override
    public void onResume(){
        super.onResume();
        initListCheckins()
    }

    private void initListCheckins(){
        CheckinRestOperations owm = retrofit.create(CheckinRestOperations)
        Call<List<Checkin>> model = owm.getCheckins()
        def callback = [
                onResponse :{Call<List<Checkin>> call, Response<List<Checkin>> response ->
                    createListView(response)
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
                MyCheckinsActivity.this.startActivity(new Intent(MyCheckinsActivity.this,CheckInActivity.class));
            }
        });

        initListCheckins()
    }

}

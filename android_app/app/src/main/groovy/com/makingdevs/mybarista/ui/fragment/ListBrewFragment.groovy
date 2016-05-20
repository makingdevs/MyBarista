package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.ApiService
import com.makingdevs.mybarista.ui.adapter.BrewAdapter
import groovy.transform.CompileStatic
import makingdevs.com.mybarista.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;


@CompileStatic
public class ListBrewFragment extends Fragment {

    RecyclerView mListBrew
    BrewAdapter mBrewAdapter

    private final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl('http://192.168.1.198:3000/')
            .build()

    ListBrewFragment(){

    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_brew,container, false)
        mListBrew = (RecyclerView) root.findViewById(R.id.list_brews)
        mListBrew.setLayoutManager(new LinearLayoutManager(getActivity()))
        updateUI()
        root
    }

    void updateUI() {
        ApiService owm = retrofit.create(ApiService)
        Call<List<Checkin>> model = owm.getCheckins()
        def callback = [
                onResponse :{ Call<List<Checkin>> call, Response<List<Checkin>> response ->
                    Log.d("TAGGGGGG", response.body().toList().toString())
                    if(!mBrewAdapter){
                        mBrewAdapter = new BrewAdapter(getActivity(), response.body().toList())
                        mListBrew.adapter = mBrewAdapter
                    } else {
                        mBrewAdapter.setmCheckins(response.body().toList())
                        mBrewAdapter.notifyDataSetChanged()
                    }
                },
                onFailure : {Call<List<Checkin>> call, Throwable t -> Log.d("ERRORZ", "el error") }
        ]
        model.enqueue(callback as Callback<List<Checkin>>)
    }
}

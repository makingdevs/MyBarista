package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.CircleFlavor
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.ui.activity.CircleFlavorActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response


@CompileStatic
public class ShowCircleFlavorFragment extends Fragment {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    private static final String TAG = "ShowCircleFlavorFragment"
    private static String ID_CIRCLE_FLAVOR

    ShowCircleFlavorFragment(String id){
        Bundle args = new Bundle()
        args.putSerializable(ID_CIRCLE_FLAVOR,id)
        this.arguments = args
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        String circleFlavorId = getArguments()?.getSerializable(ID_CIRCLE_FLAVOR)
        //mCheckinManager.showCircleFlavor(circleFlavorId,onSuccess(),onError())
        super.onCreate(savedInstanceState);

    }

    private Closure onSuccess(){
        { Call<CircleFlavor> call, Response<CircleFlavor> response ->
            Log.d(TAG,response.body().toString())
            setCircleFlavorView(response.body())
        }
    }

    private void setCircleFlavorView(CircleFlavor circleFlavor) {

    }

    private Closure onError(){
        { Call<CircleFlavor> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }
}
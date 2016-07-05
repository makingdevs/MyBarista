package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
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
    RoundCornerProgressBar sweetnessBar
    RoundCornerProgressBar acidityBar
    RoundCornerProgressBar floweryBar
    RoundCornerProgressBar spicyBar
    RoundCornerProgressBar saltyBar
    RoundCornerProgressBar berriesBar
    RoundCornerProgressBar chocolateBar
    RoundCornerProgressBar candyBar
    RoundCornerProgressBar bodyBar
    RoundCornerProgressBar cleaningBar



    ShowCircleFlavorFragment(){ super() }


    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        String circleFlavorId = getActivity().getIntent().getExtras().getString("circle_flavor_id")
        if(!circleFlavorId) return
        mCheckinManager.showCircleFlavor(circleFlavorId,onSuccess(),onError())

    }

    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_circle_flavor_grafic, container, false)
        sweetnessBar = (RoundCornerProgressBar) root.findViewById(R.id.sweetnessBar)
        acidityBar = (RoundCornerProgressBar) root.findViewById(R.id.acidityBar)
        floweryBar = (RoundCornerProgressBar) root.findViewById(R.id.floweryBar)
        spicyBar = (RoundCornerProgressBar) root.findViewById(R.id.spicyBar)
        saltyBar = (RoundCornerProgressBar) root.findViewById(R.id.saltyBar)
        berriesBar = (RoundCornerProgressBar) root.findViewById(R.id.berriesBar)
        chocolateBar = (RoundCornerProgressBar) root.findViewById(R.id.chocolateBar)
        candyBar = (RoundCornerProgressBar) root.findViewById(R.id.candyBar)
        bodyBar = (RoundCornerProgressBar) root.findViewById(R.id.bodyBar)
        cleaningBar = (RoundCornerProgressBar) root.findViewById(R.id.cleaningBar)
        root
    }

    private Closure onSuccess(){
        { Call<CircleFlavor> call, Response<CircleFlavor> response ->
            setCircleFlavorView(response.body())
        }
    }

    private void setCircleFlavorView(CircleFlavor circleFlavor) {
        Log.d(TAG,circleFlavor.dump().toString())
        setProgressInView(sweetnessBar,circleFlavor.sweetness)
        setProgressInView(sweetnessBar, circleFlavor.sweetness)
        setProgressInView(acidityBar, circleFlavor.acidity)
        setProgressInView(floweryBar, circleFlavor.flowery)
        setProgressInView(spicyBar, circleFlavor.spicy)
        setProgressInView(saltyBar, circleFlavor.salty)
        setProgressInView(berriesBar, circleFlavor.berries)
        setProgressInView(chocolateBar, circleFlavor.chocolate)
        setProgressInView(candyBar, circleFlavor.candy)
        setProgressInView(bodyBar, circleFlavor.body)
        setProgressInView(cleaningBar, circleFlavor.cleaning)
    }

    private void setProgressInView(RoundCornerProgressBar variable, String value) {
        variable.setProgressColor(Color.parseColor("#00BFFF"));
        variable.setProgressBackgroundColor(Color.parseColor("#ECECEC"));
        variable.setMax(10);
        variable.setProgress(value.toFloat());
    }

    private Closure onError(){
        { Call<CircleFlavor> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }
}
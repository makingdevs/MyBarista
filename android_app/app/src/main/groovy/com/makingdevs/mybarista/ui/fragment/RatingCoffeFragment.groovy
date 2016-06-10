package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import groovy.transform.CompileStatic
import android.widget.RatingBar.OnRatingBarChangeListener;
import retrofit2.Call
import retrofit2.Response
import android.support.v4.app.Fragment


@CompileStatic
public class RatingCoffeFragment extends Fragment {

    private static final String TAG = "RatingCoffeFragment"
    private RatingBar mRatingCoffeBar
    private static Context contextView

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    RatingCoffeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addListenerToRatingCoffeBar() {
       mRatingCoffeBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
           @Override
           void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                sendRatingCoffeToCheckin()
           }
       })
    }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rating_coffee, container, false)
        mRatingCoffeBar = (RatingBar) root.findViewById(R.id.rating_coffe_bar)
        contextView = getActivity().getApplicationContext()
        addListenerToRatingCoffeBar()

    root
    }


    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 201) {
                Log.d(TAG,response.body().toString())
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRatingCoffeToCheckin() {
        String id = getActivity().getIntent().getExtras().getString("checkingId")
        mCheckinManager.saveRating(String.valueOf(mRatingCoffeBar.getRating()),id,onSuccess(),onError())
    }

}
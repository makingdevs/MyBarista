package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.command.CheckinCommand
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
    private static String ID_CHECKIN
    private RatingBar mRatingCoffeBar
    private TextView mRatingCoffeText
    private static Context contextView

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    RatingCoffeFragment(String id) {
        Bundle args = new Bundle()
        args.putSerializable(ID_CHECKIN, id)
        this.arguments = args
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
        Log.d(TAG,checkinId)
        mCheckinManager.show(checkinId,onSuccessShow(),onError())
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
        mRatingCoffeText = (TextView) root.findViewById(R.id.rating_coffe_text)
        contextView = getActivity().getApplicationContext()
        addListenerToRatingCoffeBar()

    root
    }

    private Closure onSuccessShow() {
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,response.dump().toString())
            if (response.code() == 200) {
                setRatingCoffe(response.body())
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,response.dump().toString())
            if (response.code() == 200) {
                Log.d(TAG,response.body().toString())
                setRatingCoffe(response.body())
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
        String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
        CheckinCommand command = new CheckinCommand(rating: String.valueOf(mRatingCoffeBar.getRating()))
        mCheckinManager.saveRating(checkinId, command,onSuccess(),onError())
    }

    private void setRatingCoffe(Checkin checkin) {
        mRatingCoffeBar.setRating(Float.parseFloat(checkin.rating ?: "0"))
        mRatingCoffeText.text = checkin.rating ?: "0"
    }

}
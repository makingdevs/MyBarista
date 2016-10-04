package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class RatingCoffeeFragment extends Fragment {

    private static final String TAG = "RatingCoffeeFragment"
    static String CHECK_IN_ID = "check_in_id"
    private RatingBar mRatingCoffeeBar
    private TextView mRatingCoffeeText
    private static Context contextView
    private User currentUser
    private String mCheckinId

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    RatingCoffeeFragment() { super() }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        mCheckinId = getActivity().getIntent().getExtras().getString(CHECK_IN_ID)
        currentUser = mSessionManager.getUserSession(getContext())
        mCheckinManager.show(mCheckinId, onSuccessShow(), onError())
    }

    private void addListenerToRatingCoffeeBar() {
        mRatingCoffeeBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                sendRatingCoffeeToCheckin()
            }
        })
    }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rating_coffee, container, false)
        mRatingCoffeeBar = (RatingBar) root.findViewById(R.id.rating_coffe_bar)
        mRatingCoffeeText = (TextView) root.findViewById(R.id.rating_coffe_text)
        contextView = getActivity().getApplicationContext()
        root
    }

    private Closure onSuccessShow() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 200) {
                Checkin checkin = response.body()
                setRatingCoffee(checkin)
                addListenerToRatingCoffeeBar()
                if (checkin.author == currentUser.username)
                    mRatingCoffeeBar.isIndicator = false
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            setRatingCoffee(response.body())
            if (response.code() == 200) {
                Log.d(TAG, response.body().toString())
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

    private void sendRatingCoffeeToCheckin() {
        CheckinCommand command = new CheckinCommand(rating: String.valueOf(mRatingCoffeeBar.getRating()))
        mCheckinManager.saveRating(mCheckinId, command, onSuccess(), onError())
    }

    private void setRatingCoffee(Checkin checkin) {
        mRatingCoffeeBar.setRating(Float.parseFloat(checkin.rating ?: "0"))
        mRatingCoffeeText.text = checkin.rating ?: "0"
    }
}
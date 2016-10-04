package com.makingdevs.mybarista.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.CircleFlavor
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.activity.CircleFlavorActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCircleFlavorFragment extends Fragment {

    CheckinManager mCheckInManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    private static final String TAG = "ShowCircleFlavorFragment"
    static String CHECK_IN_ID = "check_in_id"
    static String CIRCLE_FLAVOR_ID = "circle_flavor_id"
    static String CURRENT_CHECK_IN = "check_in"

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
    Checkin currentCheckIn
    CircleFlavor currentCircleFlavor
    User currentUser

    ShowCircleFlavorFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        currentCircleFlavor = new CircleFlavor()
        currentUser = mSessionManager.getUserSession(getContext())
        currentCheckIn = getActivity().intent.extras.getSerializable(CURRENT_CHECK_IN) as Checkin
        if (!currentCheckIn.circle_flavor_id) return
        mCheckInManager.showCircleFlavor(currentCheckIn.circle_flavor_id, onSuccess(), onError())


    }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        validateCheckInAuthor()
        root
    }

    private void validateCheckInAuthor() {
        if (currentCheckIn.author == currentUser.username) {
            Closure action = {
                Intent intent = CircleFlavorActivity.newIntentWithContext(getContext())
                intent.putExtra(CHECK_IN_ID, currentCheckIn.id)
                intent.putExtra(CIRCLE_FLAVOR_ID, currentCircleFlavor)
                startActivityForResult(intent, 1)
            }
            [sweetnessBar, acidityBar, floweryBar, spicyBar, saltyBar, berriesBar, chocolateBar, candyBar, bodyBar, cleaningBar]*.setOnClickListener(action)
        }
    }

    private Closure onSuccess() {
        { Call<CircleFlavor> call, Response<CircleFlavor> response ->
            currentCircleFlavor = response.body()
            setCircleFlavorView(response.body())
        }
    }

    private void setCircleFlavorView(CircleFlavor circleFlavor) {
        Log.d(TAG, circleFlavor.dump().toString())
        setProgressInView(sweetnessBar, circleFlavor.sweetness)
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

    private Closure onError() {
        { Call<CircleFlavor> call, Throwable t -> Log.d(TAG, t.message) }
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                CircleFlavor circleUpdated = new CircleFlavor()
                circleUpdated = data.getExtras().getSerializable(CIRCLE_FLAVOR_ID) as CircleFlavor
                currentCircleFlavor = circleUpdated
                setCircleFlavorView(circleUpdated)
            }
        }
    }
}
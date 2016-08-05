package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.activity.CheckInActivity
import com.makingdevs.mybarista.ui.adapter.BrewAdapter
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ListBrewFragment extends Fragment {

    private static final String TAG = "ListBrewFragment"
    private static final String CURRENT_CHECK_IN = "check_in"
    private static final String ACTION_CHECK_IN = "action_check_in"
    RecyclerView mListBrew
    BrewAdapter mBrewAdapter
    FloatingActionButton mButtonGoChekin

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    ListBrewFragment() { super() }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_brew, container, false)
        mListBrew = (RecyclerView) root.findViewById(R.id.list_brews)
        mListBrew.setLayoutManager(new LinearLayoutManager(getActivity()))

        mButtonGoChekin = (FloatingActionButton) root.findViewById(R.id.button_go_chekin)
        mButtonGoChekin.onClickListener = {
            Intent intent = CheckInActivity.newIntentWithContext(getContext())
            intent.putExtra(ACTION_CHECK_IN, 0)
            intent.putExtra(CURRENT_CHECK_IN, new Checkin())
            startActivity(intent)
        }
        updateUI()
        root
    }

    @Override
    public void onResume() {
        super.onResume()
        updateUI()
    }

    void updateUI() {
        User currentUser = mSessionManager.getUserSession(getContext())
        mCheckinManager.list([username: currentUser.username], onSuccess(), onError())
    }

    private Closure onSuccess() {
        { Call<List<Checkin>> call, Response<List<Checkin>> response ->
            if (!mBrewAdapter) {
                mBrewAdapter = new BrewAdapter(getActivity(), response.body().toList())
                mListBrew.adapter = mBrewAdapter
            } else {
                mBrewAdapter.setmCheckins(response.body().toList())
                mBrewAdapter.notifyDataSetChanged()
            }
        }
    }

    private Closure onError() {
        { Call<List<Checkin>> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }
}

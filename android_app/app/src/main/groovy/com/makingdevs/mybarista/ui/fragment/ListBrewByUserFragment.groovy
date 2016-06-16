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
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.ui.adapter.BrewAdapter
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ListBrewByUserFragment extends Fragment {

    private static final String TAG = "ListBrewByUserFragment"
    private static String USERNAME
    RecyclerView mListBrew
    BrewAdapter mBrewAdapter
    String username = ""

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    ListBrewByUserFragment(String username){
        Bundle args = new Bundle()
        args.putSerializable(USERNAME, username)
        this.arguments = args
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(!getArguments() || !getArguments()?.getSerializable(USERNAME))
            throw new IllegalArgumentException("No arguments $USERNAME")
        username = getArguments()?.getSerializable(USERNAME)
        View root = inflater.inflate(R.layout.fragment_list_brew_by_user,container, false)
        mListBrew = (RecyclerView) root.findViewById(R.id.list_brews)
        mListBrew.setLayoutManager(new LinearLayoutManager(getActivity()))
        updateUI()
        root
    }

    @Override
    public void onResume(){
        super.onResume()
        updateUI()
    }

    void updateUI() {
        mCheckinManager.list([username:username],onSuccess(),onError())
    }

    private Closure onSuccess(){
        {Call<List<Checkin>> call, Response<List<Checkin>> response ->
            if(!mBrewAdapter){
                mBrewAdapter = new BrewAdapter(getActivity(), response.body().toList())
                mListBrew.adapter = mBrewAdapter
            } else {
                mBrewAdapter.setmCheckins(response.body().toList())
                mBrewAdapter.notifyDataSetChanged()
            }
        }
    }

    private Closure onError(){
        {Call<List<Checkin>> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }
}

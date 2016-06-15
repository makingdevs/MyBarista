package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import groovy.transform.CompileStatic

@CompileStatic
public class SearchUserFragment extends Fragment {

    private static final String TAG = "SearchUserFragment"
    RecyclerView mListUsers
    //BrewAdapter mBrewAdapter

    //CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    SearchUserFragment(){}

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_user,container, false)
        mListUsers = (RecyclerView) root.findViewById(R.id.list_users)
        mListUsers.setLayoutManager(new LinearLayoutManager(getActivity()))
        updateUI()
        root
    }

    void updateUI() {
        /*User currentUser =  mSessionManager.getUserSession(getContext())
        mCheckinManager.list([username:currentUser.username],onSuccess(),onError())*/
    }

    /*private Closure onSuccess(){
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
    }*/
}

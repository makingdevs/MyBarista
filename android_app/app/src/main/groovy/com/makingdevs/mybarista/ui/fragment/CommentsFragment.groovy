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
import com.makingdevs.mybarista.model.Comment
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.CommentManager
import com.makingdevs.mybarista.service.CommentManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.adapter.BrewAdapter
import com.makingdevs.mybarista.ui.adapter.CommentAdapter
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class CommentsFragment extends Fragment {

    private static final String TAG = "CommentsFragment"
    RecyclerView mListComments
    CommentAdapter mCommentsAdapter

    SessionManager mSessionManager = SessionManagerImpl.instance
    CommentManager mCommentManager = CommentManagerImpl.instance

    CommentsFragment(){}

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments,container, false)
        mListComments = (RecyclerView) root.findViewById(R.id.list_commnets)
        mListComments.setLayoutManager(new LinearLayoutManager(getActivity()))
        updateUI()
        root
    }

    void updateUI() {
        User currentUser =  mSessionManager.getUserSession(getContext())
        mCommentManager.list([username:currentUser.username],onSuccess(),onError())
    }

    private Closure onSuccess(){
        { Call<List<Comment>> call, Response<List<Comment>> response ->
            if(!mCommentsAdapter){
                mCommentsAdapter = new CommentAdapter(getActivity(), response.body().toList())
                mListComments.adapter = mCommentsAdapter
            } else {
                mCommentsAdapter.setmComments(response.body().toList())
                mCommentsAdapter.notifyDataSetChanged()
            }
        }
    }

    private Closure onError(){
        {Call<List<Checkin>> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }


}

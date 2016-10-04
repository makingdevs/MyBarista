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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.Comment
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.CommentCommand
import com.makingdevs.mybarista.service.CommentManager
import com.makingdevs.mybarista.service.CommentManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.adapter.CommentAdapter
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class CommentsFragment extends Fragment {

    private static final String TAG = "CommentsFragment"
    static String CHECK_IN_ID = "check_in_id"
    String checkInId
    RecyclerView mListComments
    CommentAdapter mCommentsAdapter
    ImageView mSendMessage
    EditText mCommentsText
    User currentUser

    SessionManager mSessionManager = SessionManagerImpl.instance
    CommentManager mCommentManager = CommentManagerImpl.instance

    CommentsFragment() { super() }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments, container, false)
        currentUser = mSessionManager.getUserSession(getContext())
        mListComments = (RecyclerView) root.findViewById(R.id.list_commnets)
        mSendMessage = (ImageView) root.findViewById(R.id.sendButton)
        mCommentsText = (EditText) root.findViewById(R.id.comment)
        mSendMessage.onClickListener = {
            sendMessage()
        }
        checkInId = getActivity().getIntent().getExtras().getString(CHECK_IN_ID)
        mListComments.setLayoutManager(new LinearLayoutManager(getActivity()))
        updateUI()
        root
    }

    void updateUI() {
        mCommentManager.list(checkInId, onSuccess(), onError())
    }

    void sendMessage() {
        String comment = mCommentsText.getText().toString()
        CommentCommand commentCommand = new CommentCommand(body: comment, checkin_id: checkInId, username: currentUser.username, created_at: new Date())
        mCommentManager.save(commentCommand, onSuccessComment(), onError())
    }

    private Closure onSuccess() {
        { Call<List<Comment>> call, Response<List<Comment>> response ->
            if (!mCommentsAdapter) {
                mCommentsAdapter = new CommentAdapter(getActivity(), response.body().toList())
                mListComments.adapter = mCommentsAdapter
            } else {
                mCommentsAdapter.setmComments(response.body().toList())
                mCommentsAdapter.notifyDataSetChanged()
            }
        }
    }

    private Closure onError() {
        { Call<List<Checkin>> call, Throwable t -> Log.d(TAG, t.message) }
    }

    private Closure onSuccessComment() {
        { Call<Comment> call, Response<Comment> response ->
            if (response.code() == 201) {
                mCommentsText.text = ""
                updateUI()
            } else {
                Toast.makeText(getContext(), R.string.comment_error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

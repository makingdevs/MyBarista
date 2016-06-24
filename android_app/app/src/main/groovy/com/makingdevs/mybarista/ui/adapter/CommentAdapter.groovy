package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Comment
import groovy.transform.CompileStatic

@CompileStatic
class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    Context mContext
    List<Comment> mComments

    CommentAdapter(Context context, List<Comment> commentList){
        mContext = context
        mComments = commentList
    }

    @Override
    CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false)
        new CommentViewHolder(view)
    }

    @Override
    void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mComments[position]
        holder.bindCheckin(comment)
    }

    @Override
    int getItemCount() {
        mComments.size()
    }


    class CommentViewHolder extends RecyclerView.ViewHolder{

        Comment mComment
        TextView mBody
        TextView mAuthorComment
        TextView mDateCreated

        void bindCheckin(Comment comment) {
            mComment = comment
            mBody.text = mComment.body
            mAuthorComment.text = mComment.user.username
            String fecha = DateUtils.getRelativeTimeSpanString(mComment.created_at.time, new Date().time, 0).toString()
            mDateCreated.text = fecha

        }

        CommentViewHolder(View itemView) {
            super(itemView)
            mBody = (TextView) itemView.findViewById(R.id.label_body)
            mAuthorComment = (TextView) itemView.findViewById(R.id.author_comment)
            mDateCreated = (TextView) itemView.findViewById(R.id.date_created)
        }
    }

}


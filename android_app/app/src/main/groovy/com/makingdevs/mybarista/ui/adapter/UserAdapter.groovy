package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.ui.activity.ProfilePublicActivity
import groovy.transform.CompileStatic

class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    Context mContext
    List<UserProfile> mUsers

    UserAdapter(Context context, List<UserProfile> UsersList){
        mContext = context
        mUsers = UsersList
    }

    @Override
    UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false)
        new UserViewHolder(view)
    }

    @Override
    void onBindViewHolder(UserViewHolder holder, int position) {
        UserProfile user = mUsers[position]
        holder.bindCheckin(user)
    }

    @Override
    int getItemCount() {
        mUsers.size()
    }


    class UserViewHolder extends RecyclerView.ViewHolder{

        UserProfile mUser
        TextView mUserLabel
        ImageView mAvatarUser

        void bindCheckin(UserProfile user) {
            mUser = user
            itemView.onClickListener = {
                Intent intent = ProfilePublicActivity.newIntentWithContext(mContext,mUser.id)
                mContext.startActivity(intent)
            }
            mUserLabel.text = mUser.visible_name
        }

        UserViewHolder(View itemView) {
            super(itemView)
            mUserLabel = (TextView) itemView.findViewById(R.id.user_label)
            mAvatarUser = (ImageView) itemView.findViewById(R.id.avatar_user)

        }
    }

}


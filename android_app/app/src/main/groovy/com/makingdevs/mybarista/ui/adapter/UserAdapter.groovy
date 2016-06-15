package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.User

class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    Context mContext
    List<User> mUsers

    UserAdapter(Context context, List<User> UsersList){
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
        User user = mUsers[position]
        holder.bindCheckin(user)
    }

    @Override
    int getItemCount() {
        mUsers.size()
    }


    class UserViewHolder extends RecyclerView.ViewHolder{

        User mUser
        TextView mUserLabel
        ImageView mAvatarUser

        void bindCheckin(User user) {
            mUser = user
            itemView.onClickListener = {
                //soomething
            }
            mUserLabel.text = user.username
        }

        UserViewHolder(View itemView) {
            super(itemView)
            mUserLabel = (TextView) itemView.findViewById(R.id.user_label)
            mAvatarUser = (ImageView) itemView.findViewById(R.id.avatar_user)

        }
    }

}


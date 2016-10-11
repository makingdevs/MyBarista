package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity

class BrewAdapter extends RecyclerView.Adapter<BrewViewHolder> {

    Context mContext
    List<Checkin> mCheckins
    ImageUtil mImageUtil = new ImageUtil()

    BrewAdapter(Context context, List<Checkin> checkinList) {
        mContext = context
        mCheckins = checkinList
    }

    @Override
    BrewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_checkin, parent, false)
        new BrewViewHolder(view)
    }

    @Override
    void onBindViewHolder(BrewViewHolder holder, int position) {
        Checkin checkin = mCheckins[position]
        holder.bindCheckin(checkin)
    }

    @Override
    int getItemCount() {
        mCheckins.size()
    }

    class BrewViewHolder extends RecyclerView.ViewHolder {

        Checkin mCheckin
        TextView mTitle
        TextView mState
        TextView mPrice
        TextView mMoment
        RatingBar mRating
        ImageView photoCheckinImageView

        void bindCheckin(Checkin checkin) {
            mCheckin = checkin
            itemView.onClickListener = {
                Intent intent = ShowCheckinActivity.newIntentWithContext(mContext, mCheckin)
                mContext.startActivity(intent)
            }
            mTitle.text = checkin.method
            mState.text = checkin.state
            String date = DateUtils.getRelativeTimeSpanString(mCheckin.created_at.time, new Date().time, 0).toString()
            mMoment.text = date
            mPrice.text = "${checkin.price ? "\$ ${checkin.price}" : ""}"
            mRating.setRating(Float.parseFloat(checkin.rating ?: "0"))
            if (mCheckin.s3_asset?.url_file)
                mImageUtil.setPhotoImageView(mContext, mCheckin.s3_asset?.url_file, photoCheckinImageView)
            else
                photoCheckinImageView.setImageResource(R.drawable.placeholder_coffee)

        }

        BrewViewHolder(View itemView) {
            super(itemView)
            mTitle = (TextView) itemView.findViewById(R.id.label_method)
            mState = (TextView) itemView.findViewById(R.id.label_state)
            mPrice = (TextView) itemView.findViewById(R.id.label_price)
            mMoment = (TextView) itemView.findViewById(R.id.label_moment)
            mRating = (RatingBar) itemView.findViewById(R.id.rating_coffe_bar)
            photoCheckinImageView = (ImageView) itemView.findViewById(R.id.myImageView)
        }
    }

}


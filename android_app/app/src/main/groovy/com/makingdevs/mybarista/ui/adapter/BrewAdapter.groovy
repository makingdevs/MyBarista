package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.ImageView
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity
import groovy.transform.CompileStatic

class BrewAdapter extends RecyclerView.Adapter<BrewViewHolder>{

    Context mContext
    List<Checkin> mCheckins
    ImageUtil mImageUtil1 = new ImageUtil()

    BrewAdapter(Context context, List<Checkin> checkinList){
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


    class BrewViewHolder extends RecyclerView.ViewHolder{

        Checkin mCheckin
        TextView mtitle
        TextView morigin
        TextView mprice
        TextView mmoment
        RatingBar mrating
        ImageView photoCheckinImageView

        void bindCheckin(Checkin checkin) {
            mCheckin = checkin
            itemView.onClickListener = {
                Intent intent = ShowCheckinActivity.newIntentWithContext(mContext,mCheckin.id,mCheckin.circle_flavor_id)
                mContext.startActivity(intent)
            }
            mtitle.text = checkin.method
            morigin.text = checkin.origin
            //mmoment.text = checkin.created_at.format("dd - MM - yy")
            //mmoment.text = (DateUtils.getRelativeTimeSpanString(checkin.created_at.time)).toString()
            mprice.text = checkin.price
            mrating.setRating(Float.parseFloat(checkin.rating ?: "0"))
            if(mCheckin.s3_asset?.url_file)
                mImageUtil1.setPhotoImageView(mContext, mCheckin.s3_asset?.url_file,photoCheckinImageView)

        }

        BrewViewHolder(View itemView) {
            super(itemView)
            mtitle = (TextView) itemView.findViewById(R.id.label_method)
            morigin = (TextView) itemView.findViewById(R.id.label_origin)
            mprice = (TextView) itemView.findViewById(R.id.label_price)
            mmoment = (TextView) itemView.findViewById(R.id.label_moment)
            mrating = (RatingBar) itemView.findViewById(R.id.rating_coffe_bar)
            photoCheckinImageView = (ImageView) itemView.findViewById(R.id.myImageView)

        }
    }

}


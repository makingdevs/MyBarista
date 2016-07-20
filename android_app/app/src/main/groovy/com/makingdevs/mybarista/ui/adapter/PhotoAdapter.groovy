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
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity

class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    Context mContext
    List<PhotoCheckin> mPhotos
    ImageUtil mImageUtil1 = new ImageUtil()

    PhotoAdapter(Context context, List<PhotoCheckin> photoList) {
        mContext = context
        mPhotos = photoList
    }

    @Override
    PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false)
        new PhotoViewHolder(view)
    }

    @Override
    void onBindViewHolder(PhotoViewHolder holder, int position) {
        PhotoCheckin photo = mPhotos[position]
        holder.bindCheckin(photo)
    }

    @Override
    int getItemCount() {
        mPhotos.size()
    }


    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView

        void bindCheckin(PhotoCheckin photo) {
            /*mCheckin = checkin
            itemView.onClickListener = {
                Intent intent = ShowCheckinActivity.newIntentWithContext(mContext,mCheckin.id,mCheckin.circle_flavor_id)
                mContext.startActivity(intent)
            }
            mtitle.text = checkin.method
            morigin.text = checkin.origin
            String fecha = DateUtils.getRelativeTimeSpanString(mCheckin.created_at.time, new Date().time, 0).toString()
            mmoment.text = fecha
            mprice.text = "${checkin.price ? "\$ ${checkin.price}" : ""}"
            mrating.setRating(Float.parseFloat(checkin.rating ?: "0"))
            if(mCheckin.s3_asset?.url_file)
                mImageUtil1.setPhotoImageView(mContext, mCheckin.s3_asset?.url_file, photoImageView)
            else
                photoImageView.setImageResource(R.drawable.coffee)*/

        }

        PhotoViewHolder(View itemView) {
            super(itemView)
            photoImageView = (ImageView) itemView.findViewById(R.id.photo_gallery)

        }
    }

}


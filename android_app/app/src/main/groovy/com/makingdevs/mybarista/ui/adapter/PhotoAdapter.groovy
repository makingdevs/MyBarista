package com.makingdevs.mybarista.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.OnItemClickListener
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.service.S3assetManager
import com.makingdevs.mybarista.service.S3assetManagerImpl

class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    Context mContext
    List<PhotoCheckin> mPhotos
    ImageUtil mImageUtil1 = new ImageUtil()
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    OnItemClickListener<PhotoCheckin> onItemClickListener


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

    void setOnClickListener(OnItemClickListener<PhotoCheckin> onItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView photoImageView

        void bindCheckin(PhotoCheckin photo) {
            mImageUtil1.setPhotoImageView(mContext, photo.url_file, photoImageView)
        }

        PhotoViewHolder(View itemView) {
            super(itemView)
            photoImageView = (ImageView) itemView.findViewById(R.id.photo_gallery)
            itemView.setOnClickListener(this)
        }

        @Override
        void onClick(View v) {
            if (onItemClickListener)
                onItemClickListener.onItemClicked(mPhotos.get(getAdapterPosition()))
        }
    }

}


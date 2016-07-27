package com.makingdevs.mybarista.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import groovy.transform.CompileStatic

@CompileStatic
trait OnActivityResultGallery {
    ImageView showImage
    ImageUtil mImageUtil1 = new ImageUtil()
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mImageUtil1.setPhotoImageView(getContext(),data.getStringExtra("PATH_PHOTO") , showImage)
            }
        }
    }
    abstract Context getContext()
}
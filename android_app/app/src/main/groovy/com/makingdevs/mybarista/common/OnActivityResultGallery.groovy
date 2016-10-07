package com.makingdevs.mybarista.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.facebook.CallbackManager
import groovy.transform.CompileStatic

@CompileStatic
trait OnActivityResultGallery {
    ImageView showImage
    ImageUtil mImageUtil1 = new ImageUtil()
    String pathPhoto
    CallbackManager callbackManager
    ImageView addPhotoCheckIn
    ImageView transparencyView

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                pathPhoto = data.getStringExtra("PATH_PHOTO")
                mImageUtil1.setPhotoImageView(getContext(), pathPhoto, showImage)
                addPhotoCheckIn.setVisibility(View.GONE)
                transparencyView.setVisibility(View.GONE)
            }
        }
    }

    abstract Context getContext()
}
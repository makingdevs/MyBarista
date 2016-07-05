package com.makingdevs.mybarista.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import com.makingdevs.mybarista.common.CamaraUtil
import com.makingdevs.mybarista.common.ImageUtil
import groovy.transform.CompileStatic

@CompileStatic
public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment"
    private static Context contextView

    static final int REQUEST_TAKE_PHOTO = 0

    CamaraUtil mCamaraUtil = new CamaraUtil()
    File photoFile
    ImageUtil mImageUtil
    Closure successActionOnPhoto
    Closure errorActionOnPhoto

    CameraFragment(){ super() }

    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        contextView = getActivity().getApplicationContext()
        dispatchTakePictureIntent()
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            try {
                photoFile = mCamaraUtil.createImageFile()
            } catch (IOException ex) {
                Log.d(TAG,"Error al crear la foto $ex")
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap bitmapResize = mCamaraUtil.resizeBitmapFromFilePath(photoFile.getPath(),1024,720)
            File photo = mCamaraUtil.saveBitmapToFile(bitmapResize,photoFile.getName())
            mImageUtil.addPictureToGallery(getContext(),photo.getPath())
            successActionOnPhoto(photo)
        } else {
            errorActionOnPhoto()
        }
    }

}
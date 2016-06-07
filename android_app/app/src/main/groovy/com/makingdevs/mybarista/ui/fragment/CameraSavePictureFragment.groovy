package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.ui.activity.CameraSavePictureActivity
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity

class CameraSavePictureFragment extends Fragment{
    CameraSavePictureFragment(){}

    private static final String TAG = "CameraSavePictureFragment"
    private ImageView previewCamera
    private Button mButtonAcceptPhoto
    private Button mButtonCancelPhoto

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera_save_picture,container, false)
        previewCamera = root.findViewById(R.id.previewCamera)
        mButtonAcceptPhoto = (Button) root.findViewById(R.id.btnAcceptPhoto)
        mButtonCancelPhoto = (Button) root.findViewById(R.id.btnCanelPhoto)
        CameraSavePictureActivity activity=(CameraSavePictureActivity)getActivity()
        Bundle extras = activity.getUriFile()
        Bitmap imageBitmap = (Bitmap) extras.get("data")
        previewCamera.setImageBitmap(imageBitmap)
        mButtonAcceptPhoto.onClickListener = {
            Log.d(TAG,"aceptar foto")
        }
        mButtonCancelPhoto.onClickListener = {
            Log.d(TAG,"cancelar foto")
        }
        root
    }


}

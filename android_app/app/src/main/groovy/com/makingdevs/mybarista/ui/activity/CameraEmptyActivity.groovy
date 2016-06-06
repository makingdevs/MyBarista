package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.CameraEmptyFragment

class CameraEmptyActivity extends SingleFragmentActivity{

    private static final String TAG = "CameraEmptyActivity"
    static final int REQUEST_TAKE_PHOTO = 1
    static final int REQUEST_IMAGE_CAPTURE = 1

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, CameraEmptyActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new CameraEmptyFragment()
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        useCamera()
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d(TAG,"Previo de img")
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //previewCaptureCheckin.setImageBitmap(imageBitmap);
        }
    }*/
    private void useCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
               Log.d(TAG,"Error al guardar la foto $ex")
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }


    private File createImageFile(){
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        String imageFileName = "Checkin_$timeStamp"
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir)
    }
}
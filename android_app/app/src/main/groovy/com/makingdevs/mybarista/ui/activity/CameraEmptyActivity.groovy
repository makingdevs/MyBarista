package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.CameraEmptyFragment

class CameraEmptyActivity extends SingleFragmentActivity{

    private static final String TAG = "CameraEmptyActivity"
    static final int REQUEST_TAKE_PHOTO = 0

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
        dispatchTakePictureIntent()
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras()
            Intent cameraSaveIntent=new Intent(getApplicationContext(),CameraSavePictureActivity.class);
            cameraSaveIntent.putExtras(extras)
            startActivity(cameraSaveIntent)
        } else {
            Toast.makeText(this, "Error al caputar la foto", Toast.LENGTH_SHORT).show()
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null
            try {
                photoFile = createImageFile()
            } catch (IOException ex) {
                Log.d(TAG,"Error al crear la foto $ex")
            }
            if (photoFile != null) {
                takePictureIntent.putExtra("URI", Uri.fromFile(photoFile))
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private File createImageFile(){
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        String imageFileName = "Checkin_$timeStamp"
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        File image = File.createTempFile(imageFileName,".jpg", storageDir)
    }

}
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
import android.widget.ImageView
import android.widget.Toast
import com.makingdevs.mybarista.common.CamaraUtil
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.S3_Asset
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.service.UserManager
import com.makingdevs.mybarista.service.UserManagerImpl
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment"
    private static Context contextView
    private static String CHECKIN = "checkin"

    static final int REQUEST_TAKE_PHOTO = 0


    UserManager mUserManager = UserManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance


    CamaraUtil mCamaraUtil = new CamaraUtil()
    File photoFile
    ImageUtil mImageUtil
    Checkin mCheckin
    User currentUser


    CameraFragment(){
    }

    void setmCheckin(Checkin mCheckin) {
        this.mCheckin = mCheckin
    }

    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        currentUser = mSessionManager.getUserSession(getContext())
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
            Bitmap bitmapResize = mCamaraUtil.resizeBitmapFromFilePath(photoFile.getPath(),1280,960)
            File photo = mCamaraUtil.saveBitmapToFile(bitmapResize,photoFile.getName())
            mUserManager.upload(new UploadCommand(idCheckin: mCheckin.id,idUser:currentUser.id,pathFile: photo.getPath()),onSuccessPhoto(),onError())
            mImageUtil.addPictureToGallery(getContext(),photo.getPath())

        } else {
            Toast.makeText(getContext(), "Error al caputar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    private Closure onError(){
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error "+t.message) }
    }

    private Closure onSuccessPhoto(){
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            if (response.body()) {
                mCheckin.setS3_asset(new S3_Asset(url_file: response.body().url_file))
            } else {
                mCheckin.setS3_asset(new S3_Asset(url_file: "http://mybarista.com.s3.amazonaws.com/coffee.jpg"))
            }
        }
    }

}
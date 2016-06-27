package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.command.BaristaCommand
import com.makingdevs.mybarista.model.command.UploadPhotoBaristaCommand
import com.makingdevs.mybarista.service.BaristaManager
import com.makingdevs.mybarista.service.BaristaManagerImpl
import com.makingdevs.mybarista.service.S3assetManager
import com.makingdevs.mybarista.service.S3assetManagerImpl
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class BaristaFragment extends Fragment {


    private static final String TAG = "BaristaFragment"
    private EditText mNameBarista
    private Button  mButtonCreateBarista
    private static Context contextView
    private ImageView mPhotoBarista
    private ImageButton mButtonPhotoBarista
    private String mCheckinId
    ImageButton mImageButtonFoursquare

    ImageUtil mImageUtil1 = new ImageUtil()
    BaristaManager mBaristaManager = BaristaManagerImpl.instance
    S3assetManager mS3Manager = S3assetManagerImpl.instance



    View onCreateView(LayoutInflater inflater,
                       @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCheckinId = getActivity().getIntent().getExtras().getString("checkingId")
        View root = inflater.inflate(R.layout.fragment_new_barista,container, false)
        mNameBarista = (EditText) root.findViewById(R.id.name_barista_field)
        mButtonCreateBarista = (Button) root.findViewById(R.id.button_new_barista)
        mPhotoBarista = (ImageView) root.findViewById(R.id.show_photo_barista)
        mButtonPhotoBarista = (ImageButton) root.findViewById(R.id.button_camera)
        mImageButtonFoursquare = (ImageButton) root.findViewById(R.id.button_foursquare)
        mImageUtil1.setPhotoImageView(getContext(), "http://mybarista.com.s3.amazonaws.com/coffee.jpg", mPhotoBarista)
        bindingElements()

        root

    }

    private BaristaCommand getPropertiesOfBarista() {
        String name = mNameBarista.getText().toString()
        new BaristaCommand(name:  name)
    }

    private void bindingElements() {
        mButtonCreateBarista.onClickListener = {
            saveBarista(getPropertiesOfBarista(), mCheckinId)
        }
        mButtonPhotoBarista.onClickListener = {
            Fragment cameraFragment = new CameraFragment()
            cameraFragment.setSuccessActionOnPhoto { File photo ->
                mS3Manager.uploadPhotoBarista(new UploadPhotoBaristaCommand(idBarista: "1", pathFile:photo.getPath()),onSuccessPhoto(),onErrorPhoto())
            }
            cameraFragment.setErrorActionOnPhoto {
                Toast.makeText(getContext(), "Error al caputar la foto", Toast.LENGTH_SHORT).show()
            }
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, cameraFragment)
                    .addToBackStack(null).commit()
        }
        mImageButtonFoursquare.onClickListener = {

        }
    }

    private void saveBarista(BaristaCommand command, String id) {
        mBaristaManager.save(command,id,onSuccess(), onError())
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 200) {
               showCheckin(response.body())
            } else {
                Log.d(TAG, "Errorrrrz")
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            println("EXITO...")
            //if (response.body())
            //    mImageUtil1.setPhotoImageView(getContext(), response.body().url_file, photoCheckinImageView)
        }
    }

    private Closure onErrorPhoto() {
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error " + t.message) }
    }

    private void showCheckin(Checkin checkin){
        Intent intent = ShowCheckinActivity.newIntentWithContext(getContext(),checkin.id, checkin.circle_flavor_id)
        intent.putExtra("circle_flavor_id",checkin.circle_flavor_id)
        startActivity(intent)
        getActivity().finish()
    }
}
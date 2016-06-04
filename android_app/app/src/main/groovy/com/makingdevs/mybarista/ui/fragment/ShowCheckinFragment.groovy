package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.ui.activity.CameraEmptyActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

import java.text.SimpleDateFormat

@CompileStatic
public class ShowCheckinFragment extends Fragment {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    private static final String TAG = "ShowCheckinFragment"
    private static String ID_CHECKIN
    TextView mOrigin
    TextView mMethod
    TextView mPrice
    TextView mNote
    TextView mDateCreated
    View itemView
    ImageButton mButtonCamera
    ImageView mImageCamera

    ShowCheckinFragment(String id){
        Bundle args = new Bundle()
        args.putSerializable(ID_CHECKIN, id)
        this.arguments = args
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        if(!getArguments() || !getArguments()?.getSerializable(ID_CHECKIN))
            throw new IllegalArgumentException("No arguments $ID_CHECKIN")
        String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
        mCheckinManager.show(checkinId,onSuccess(),onError())
    }

    @Override
    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_chek_in, container, false)
        itemView = root
        root
    }

    private Closure onSuccess(){
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,response.body().toString())
            setCheckinInView(response.body())
        }
    }

    private Closure onError(){
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }

    private void setCheckinInView(Checkin checkin){
        mOrigin = (TextView) itemView.findViewById(R.id.origin_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        //mDateCreated  = (TextView) itemView.findViewById(R.id._data)
        mButtonCamera = (ImageButton) itemView.findViewById(R.id.button_camera)
        mButtonCamera.onClickListener = {
            Log.d(TAG,"camara...")
            Intent intent = CameraEmptyActivity.newIntentWithContext(getActivity())
            startActivity(intent)

        }

        mOrigin.text = checkin.origin
        mMethod.text = checkin.method
        mPrice.text = checkin.price
        mNote.text = checkin.note
    }

}
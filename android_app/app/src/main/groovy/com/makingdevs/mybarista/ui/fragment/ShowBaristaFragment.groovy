package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Barista
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.BaristaManager
import com.makingdevs.mybarista.service.BaristaManagerImpl
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic

class ShowBaristaFragment extends Fragment{

    ShowBaristaFragment() {}

    BaristaManager mBaristaManager = BaristaManagerImpl.instance
    ImageUtil mImageUtil1 = new ImageUtil()

    ImageView mImageViewPhotoBarista
    TextView mTextViewNameBarista
    String idBarista

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_show_barista, container, false)
        mImageViewPhotoBarista = (ImageView) root.findViewById(R.id.show_picture_barista)
        mTextViewNameBarista = (TextView) root.findViewById(R.id.name_barista)
        mBaristaManager.show(idBarista,onSuccess(),onError())
        root
    }

    Closure onSuccess() {
        { Call<Barista> call, Response<Barista> response ->
            String url_image = response?.body()?.s3_asset?.url_file
            mTextViewNameBarista.text = response?.body()?.name?.toString()
            if (url_image){
                mImageUtil1.setPhotoImageView(getContext(),url_image, mImageViewPhotoBarista)
            }
        }
    }

    Closure onError() {
        { Call<Barista> call, Throwable t -> println("el error " + t.message) }
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        Bundle bundle
        bundle = getArguments()
        idBarista = bundle?.get("ID_BARISTA").toString()
    }

}
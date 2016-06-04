package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.makingdevs.mybarista.R

class CameraEmptyFragment extends Fragment{
    CameraEmptyFragment(){}

    ImageView previewCaptureCheckin

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera_empty,container, false)
        previewCaptureCheckin = root.findViewById(R.id.previewCaptureCheckin)
        root
    }
}
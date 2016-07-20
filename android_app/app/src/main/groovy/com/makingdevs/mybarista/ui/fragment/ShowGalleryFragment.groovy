package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingdevs.mybarista.R

class ShowGalleryFragment extends Fragment{

    ShowGalleryFragment(){super()}
    RecyclerView recyclerViewPhotos

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_gallery, container, false)
//        recyclerViewPhotos = (RecyclerView) root.findViewById(R.id.list_photos)
//        recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),3))
        root
    }

}
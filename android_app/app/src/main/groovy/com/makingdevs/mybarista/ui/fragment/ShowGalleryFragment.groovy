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
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.ui.adapter.PhotoAdapter

class ShowGalleryFragment extends Fragment {

    ShowGalleryFragment() { super() }
    RecyclerView recyclerViewPhotos
    PhotoAdapter photoAdapter
    ImageUtil imageUtil = new ImageUtil()

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_gallery, container, false)
        recyclerViewPhotos = (RecyclerView) root.findViewById(R.id.list_photos)
        recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3))
        root
    }

    @Override
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState)
        photoAdapter = new PhotoAdapter(getActivity(), populateGallery())
        recyclerViewPhotos.adapter = photoAdapter
    }

    List<PhotoCheckin> populateGallery() {
        ArrayList<PhotoCheckin> gallery = new ArrayList<PhotoCheckin>()
        ArrayList<String> pathPhotos = new ArrayList<String>()
        pathPhotos = imageUtil.getGalleryPhotos(getActivity())
        pathPhotos.each {photo->
            gallery << new PhotoCheckin(url_file:photo)
        }
        gallery
    }

}
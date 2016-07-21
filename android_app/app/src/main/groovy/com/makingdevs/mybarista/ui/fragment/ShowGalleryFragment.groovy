package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.OnItemClickListener
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.service.S3assetManager
import com.makingdevs.mybarista.service.S3assetManagerImpl
import com.makingdevs.mybarista.ui.adapter.PhotoAdapter
import retrofit2.Call
import retrofit2.Response

class ShowGalleryFragment extends Fragment implements OnItemClickListener<PhotoCheckin> {

    ShowGalleryFragment() { super() }
    RecyclerView recyclerViewPhotos
    PhotoAdapter photoAdapter
    ImageUtil imageUtil = new ImageUtil()
    String currentUser
    String checkinId
    S3assetManager mS3Manager = S3assetManagerImpl.instance

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
        photoAdapter.setOnItemClickListener(this)
        recyclerViewPhotos.adapter = photoAdapter
        Bundle bundle = new Bundle()
        bundle = getArguments()
        currentUser = bundle.getString("USERID")
        checkinId = bundle.getString("CHECKINID")
    }

    List<PhotoCheckin> populateGallery() {
        ArrayList<PhotoCheckin> gallery = new ArrayList<PhotoCheckin>()
        ArrayList<String> pathPhotos = new ArrayList<String>()
        pathPhotos = imageUtil.getGalleryPhotos(getActivity())
        pathPhotos.each { photo ->
            gallery << new PhotoCheckin(url_file: photo)
        }
        gallery
    }

    @Override
    void onItemClicked(PhotoCheckin photoCheckin) {
        mS3Manager.upload(new UploadCommand(idCheckin: checkinId, idUser: currentUser, pathFile: photoCheckin.url_file), onSuccessPhoto(), onError())

    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            println("Respuesta " + response.body())
            if (response.body())
                getActivity().onBackPressed()
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error " + t.message) }
    }

}
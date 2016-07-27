package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.OnActivityResultGallery
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.RequestPermissionAndroid
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.command.BaristaCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity
import com.makingdevs.mybarista.ui.activity.ShowGalleryActivity
import retrofit2.Call
import retrofit2.Response
//@CompileStatic
class BaristaFragment extends Fragment implements OnActivityResultGallery {


    private static final String TAG = "BaristaFragment"
    private EditText mNameBarista
    private Button mButtonCreateBarista
    private ImageButton mButtonPhotoBarista
    private String mCheckinId
    ImageButton mButtonShowBarista
    Checkin checkin

    ImageUtil mImageUtil = new ImageUtil()
    BaristaManager mBaristaManager = BaristaManagerImpl.instance
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()

    BaristaFragment() {
        super()

    }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCheckinId = getActivity().getIntent().getExtras().getString("checkingId")
        View root = inflater.inflate(R.layout.fragment_new_barista, container, false)
        mNameBarista = (EditText) root.findViewById(R.id.name_barista_field)
        mButtonCreateBarista = (Button) root.findViewById(R.id.button_new_barista)
        showImage = (ImageView) root.findViewById(R.id.show_photo_barista)
        mButtonPhotoBarista = (ImageButton) root.findViewById(R.id.button_camera)
        mButtonShowBarista = (ImageButton) root.findViewById(R.id.button_show_barista)
        showImage.setImageResource(R.drawable.coffee)
        bindingElements()
        if (!checkin)
            mCheckinManager.show(mCheckinId, onSuccessGetCheckin(), onError())
        root
    }

    @Override
    void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState)
        if(checkin?.baristum?.s3_asset?.url_file){
            mImageUtil.setPhotoImageView(getContext(), checkin.baristum.s3_asset.url_file , getPhotoImageView())
        }
    }

    private BaristaCommand getPropertiesOfBarista() {
        String name = mNameBarista.getText().toString()
        new BaristaCommand(name: name, s3_asset: checkin.baristum.s3_asset.id)
    }

    private void bindingElements() {
        mButtonCreateBarista.onClickListener = {
            saveBarista(getPropertiesOfBarista(), mCheckinId)
        }
        mButtonPhotoBarista.onClickListener = {
            Intent intent = ShowGalleryActivity.newIntentWithContext(getContext())
            intent.putExtra("CONTAINER", "barista")
            startActivityForResult(intent, 1)

        }
        mButtonShowBarista.onClickListener = {
            ShowBaristaFragment showBaristaFragment = new ShowBaristaFragment()
            Bundle bundle =  new Bundle()
            bundle.putString("ID_BARISTA", checkin.baristum.id)
            showBaristaFragment.setArguments(bundle)
            changeFragment(showBaristaFragment)
        }
    }

    private void saveBarista(BaristaCommand command, String id) {
        mBaristaManager.save(command, id, onSuccess(), onError())
    }

    private Closure onSuccessGetCheckin() {
        { Call<Checkin> call, Response<Checkin> response ->
            checkin = response.body()
            if (checkin?.baristum?.s3_asset?.url_file)
                mImageUtil.setPhotoImageView(getContext(),checkin?.baristum?.s3_asset?.url_file, getShowImage())
            if (checkin.baristum.id)
                mButtonCreateBarista.text = "Actualizar barista"
            mNameBarista.text = checkin?.baristum?.name
        }
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

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            if (getFragmentManager().getBackStackEntryCount() > 0){
                getFragmentManager().popBackStack()
                checkin.baristum.s3_asset.id = response?.body()?.id
                checkin.baristum.s3_asset.url_file = response?.body()?.url_file
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Log.d(TAG,"Error ${t.message}")
        }
    }

    private void showCheckin(Checkin checkin) {
        Intent intent = ShowCheckinActivity.newIntentWithContext(getContext(), checkin.id, checkin.circle_flavor_id)
        intent.putExtra("circle_flavor_id", checkin.circle_flavor_id)
        startActivity(intent)
        getActivity().finish()
    }

    void changeFragment(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit()
    }

}
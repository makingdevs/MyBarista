package com.makingdevs.mybarista.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.OnActivityResultGallery
import com.makingdevs.mybarista.common.RequestPermissionAndroid
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.command.BaristaCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.ShowGalleryActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class BaristaFragment extends Fragment implements OnActivityResultGallery {

    static final String TAG = "BaristaFragment"
    private static final String CURRENT_CHECK_IN = "check_in"
    private static final String CHECK_IN_ID = "check_in_id"
    private static final String EXTRA_GALLERY_PHOTO = "PATH_PHOTO"
    private static final int GALLERY_REQUEST_CODE = 1
    private ImageView checkInPhoto
    private ImageUtil mImageUtil
    private String pathPhoto
    EditText mNameBarista
    Button mButtonCreateBarista
    ImageButton mButtonPhotoBarista
    String mCheckinId
    ImageButton mButtonShowBarista
    Checkin checkin

    BaristaManager mBaristaManager = BaristaManagerImpl.instance
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()


    BaristaFragment() { super() }

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_barista, container, false)
        mCheckinId = getActivity().getIntent().getExtras().getString(CHECK_IN_ID)
        mImageUtil = new ImageUtil()
        mNameBarista = (EditText) root.findViewById(R.id.name_barista_field)
        mButtonCreateBarista = (Button) root.findViewById(R.id.button_new_barista)
        checkInPhoto = (ImageView) root.findViewById(R.id.show_photo_barista)
        mButtonPhotoBarista = (ImageButton) root.findViewById(R.id.button_camera)
        mButtonShowBarista = (ImageButton) root.findViewById(R.id.button_show_barista)
        checkInPhoto.setImageResource(R.drawable.cafe)
        bindingElements()
        if (!checkin)
            mCheckinManager.show(mCheckinId, onSuccessGetCheckin(), onError())
        root
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                pathPhoto = data.getStringExtra(EXTRA_GALLERY_PHOTO)
                mImageUtil.setPhotoImageView(context, pathPhoto, checkInPhoto)
            }
        }
    }

    @Override
    void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState)
        if (checkin?.baristum?.s3_asset?.url_file) {
            mImageUtil.setPhotoImageView(getContext(), checkin.baristum.s3_asset.url_file, checkInPhoto)
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
            Bundle bundle = new Bundle()
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
                mImageUtil.setPhotoImageView(getContext(), checkin?.baristum?.s3_asset?.url_file, checkInPhoto)
            if (checkin.baristum.id)
                mButtonCreateBarista.setText(R.string.label_update)
            mNameBarista.text = checkin?.baristum?.name
        }
    }


    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 200) {
                Intent intent = new Intent()
                intent.putExtra(CURRENT_CHECK_IN, response.body())
                getActivity().setResult(Activity.RESULT_OK, intent)
                getActivity().finish()
            }
        }
    }

    private static Closure onError() {
        { Call<Checkin> call, Throwable t ->
            // Our code here
        }
    }

    void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit()
    }
}
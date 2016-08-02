package com.makingdevs.mybarista.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.common.OnActivityResultGallery
import com.makingdevs.mybarista.common.RequestPermissionAndroid
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
import com.makingdevs.mybarista.ui.activity.ShowGalleryActivity
import com.makingdevs.mybarista.view.LoadingDialog
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class FormCheckinFragment extends Fragment implements OnActivityResultGallery {

    static final String TAG = "FormCheckinFragment"
    static Context contextView
    EditText originEditText
    EditText priceEditText
    Spinner methodFieldSprinner
    Button checkInButton
    TextView venueDescription
    List<Venue> venues = [new Venue(name: "Selecciona lugar")]
    Venue venue
    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()
    ImageView imageViewPhotoCheckin
    ImageView imageViewAddVenue
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    String idS3asset
    LoadingDialog loadingDialog = LoadingDialog.newInstance(R.string.message_uploading_photo)
    ImageUtil mImageUtil1 = new ImageUtil()

    FormCheckinFragment() { super() }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_chek_in, container, false)
        venue = new Venue()
        originEditText = (EditText) root.findViewById(R.id.originField)
        priceEditText = (EditText) root.findViewById(R.id.priceField)
        methodFieldSprinner = (Spinner) root.findViewById(R.id.methodSpinner)
        checkInButton = (Button) root.findViewById(R.id.btnCheckIn)
        venueDescription = (TextView) root.findViewById(R.id.venue_description)
        venueDescription.setVisibility(View.GONE)
        contextView = getActivity().getApplicationContext()
        showImage = (ImageView) root.findViewById(R.id.preview_checkin)
        imageViewPhotoCheckin = (ImageView) root.findViewById(R.id.image_view_photo_checkin)
        imageViewAddVenue = (ImageView) root.findViewById(R.id.btnAddVenue)

        /**
         * Select a picture from the preview image
         */
        showImage.onClickListener = { chooseCheckInImage() }

        /**
         * Select a picture from the camera icon
         */
        imageViewPhotoCheckin.onClickListener = { chooseCheckInImage() }

        imageViewAddVenue.onClickListener = {
            if (checkPermissionLocation()) {
                requestPermissionAndroid.checkPermission(getActivity(), "location")
            } else {
                showSearchVenueFragment()
            }
        }

        checkInButton.onClickListener = { View v -> createCheckin() }

        root
    }

    void chooseCheckInImage() {
        if (checkPermissionStorage())
            requestPermissionAndroid.checkPermission(getActivity(), "storage")
        else
            showGalleryActivity()
    }

    void showGalleryActivity() {
        Intent intent = ShowGalleryActivity.newIntentWithContext(getContext())
        intent.putExtra("CONTAINER", "new_checkin")
        startActivityForResult(intent, 1)
    }

    void showSearchVenueFragment() {
        Fragment venueFragment = new SearchVenueFoursquareFragment()
        venueFragment.onVenueSelectedWithFragmentManagerControl = { Venue venue ->
            this.venue.name = venue.name
            this.venue.id = venue.id
            this.venueDescription.text = venue.name
            this.venueDescription.setVisibility(View.VISIBLE)
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fgm_checkin_container, venueFragment)
                .addToBackStack(null)
                .commit()
    }

    //TODO: Reestructurar métodos para el checkin
    void createCheckin() {
        if (pathPhoto) {
            loadingDialog.show(getActivity().getSupportFragmentManager(), "Loading dialog")
            mS3Manager.uploadPhoto(new UploadCommand(pathFile: pathPhoto), onSuccessPhoto(), onError())
        } else
            getFormCheckIn("")
    }

    private void getFormCheckIn(String assetID) {
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        User currentUser = mSessionManager.getUserSession(getContext())
        saveCheckIn(new CheckinCommand(method: method, origin: origin, price: price?.toString(), username: currentUser.username,
                idVenueFoursquare: venue.id, created_at: new Date(), idS3asset: assetID)
        )
    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            idS3asset = response.body().id
            getFormCheckIn(idS3asset)
            loadingDialog.dismiss()
        }
    }

    private void saveCheckIn(CheckinCommand checkin) {
        mCheckinManager.save(checkin, onSuccess(), onError())
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG, response.dump().toString())
            if (response.code() == 201) {
                Intent intent = PrincipalActivity.newIntentWithContext(getContext())
                startActivity(intent)
                getActivity().finish()
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss()
        }
    }

    void setVenuesToSpinner(Spinner spinner, List<Venue> venues) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, venues?.name)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter
    }

    boolean checkPermissionLocation() {
        Boolean status
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            status = true
        }
        status
    }

    boolean checkPermissionStorage() {
        Boolean status
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            status = true
        }
        status
    }
}
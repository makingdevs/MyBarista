package com.makingdevs.mybarista.ui.fragment

import android.Manifest
import android.app.Activity
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
import com.makingdevs.mybarista.ui.activity.ShowGalleryActivity
import com.makingdevs.mybarista.view.LoadingDialog
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class FormCheckinFragment extends Fragment implements OnActivityResultGallery {

    static final String TAG = "FormCheckinFragment"
    private static final String CURRENT_CHECK_IN = "check_in"
    private static final String ACTION_CHECK_IN = "action_check_in"
    private static final int CREATE_CHECK_IN = 0
    private static final int UPDATE_CHECK_IN = 1
    int CHECK_IN_STATUS = 0

    static Context contextView
    Button checkInButton
    List<Venue> venues = [new Venue(name: "Selecciona lugar")]
    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()
    ImageView imageViewAddVenue
    S3assetManager mS3Manager = S3assetManagerImpl.instance
    String idS3asset
    LoadingDialog loadingDialog = LoadingDialog.newInstance(R.string.message_uploading_photo)
    ImageUtil mImageUtil = new ImageUtil()
    TextView venueDescription
    Checkin currentCheckin
    Venue venue

    /**
     * Elementos por setear
     */
    Spinner methodFieldSprinner
    EditText originEditText
    EditText priceEditText
    private Spinner statesSpinner

    FormCheckinFragment() { super() }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        CHECK_IN_STATUS = getActivity().getIntent().getExtras().getInt(ACTION_CHECK_IN)
        currentCheckin = new Checkin()
        venue = new Venue()
        currentCheckin = (Checkin) activity.intent.extras.getSerializable(CURRENT_CHECK_IN)
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_chek_in, container, false)
        checkInButton = (Button) root.findViewById(R.id.btnCheckIn)
        venueDescription = (TextView) root.findViewById(R.id.venue_description)
        contextView = getActivity().getApplicationContext()
        imageViewAddVenue = (ImageView) root.findViewById(R.id.btnAddVenue)
        showImage = (ImageView) root.findViewById(R.id.preview_checkin)
        methodFieldSprinner = (Spinner) root.findViewById(R.id.methodSpinner)
        originEditText = (EditText) root.findViewById(R.id.originField)
        priceEditText = (EditText) root.findViewById(R.id.priceField)
        addPhotoCheckIn = (ImageView) root.findViewById(R.id.imgAddPhoto)
        transparencyView = (ImageView) root.findViewById(R.id.imgTransparency)
        statesSpinner = (Spinner) root.findViewById(R.id.stateSpinner)

        /**
         * Select a picture from the preview image
         */
        addPhotoCheckIn.onClickListener = { chooseCheckInImage() }

        setVenueListener()

        checkInButton.onClickListener = { View v -> createCheckin() }

        root
    }

    void setVenueListener() {
        Closure venueAction = {
            if (checkPermissionLocation())
                requestPermissionAndroid.checkPermission(getActivity(), "location")
            else
                showSearchVenueFragment()
        }
        [imageViewAddVenue, venueDescription]*.setOnClickListener(venueAction as View.OnClickListener)
    }

    @Override
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState)
        if (currentCheckin)
            if (currentCheckin.s3_asset)
                mImageUtil.setPhotoImageView(getContext(), currentCheckin.s3_asset.url_file, showImage)

        originEditText.setText(currentCheckin.origin)
        priceEditText.setText(currentCheckin.price)

        methodFieldSprinner.setAdapter(getSpinnerAdapter())
        methodFieldSprinner.setSelection(getSpinnerAdapter().getPosition(currentCheckin.method))

        statesSpinner.setAdapter(getStateSpinnerAdapter())
        statesSpinner.setSelection(getStateSpinnerAdapter().getPosition(currentCheckin.state))

        if (CHECK_IN_STATUS == UPDATE_CHECK_IN) {
            checkInButton.setText(R.string.button_update_checkin)
        }
    }

    ArrayAdapter<String> getSpinnerAdapter() {
        List<String> method = [
                "Expresso",
                "Americano",
                "Goteo",
                "Prensa",
                "Sifón",
                "Otro"
        ]
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, method)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter
    }

    ArrayAdapter<String> getStateSpinnerAdapter() {
        List<String> states = [
                "Veracruz",
                "Chiapas",
                "Guerrero",
                "Oaxaca",
                "Puebla"
        ]
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, states)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter
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
            mS3Manager.uploadPhoto(new UploadCommand(pathFile: pathPhoto), onSuccessPhoto(), onErrorCreateCheckin())
        } else
            getFormCheckIn(currentCheckin?.s3_asset?.id)
    }

    private void getFormCheckIn(String assetID) {
        CheckinCommand checkinCommand
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        String state = statesSpinner.getSelectedItem().toString()
        User currentUser = mSessionManager.getUserSession(getContext())

        switch (CHECK_IN_STATUS) {
            case CREATE_CHECK_IN:
                checkinCommand = new CheckinCommand(method: method, state: state, origin: origin, price: price?.toString(), username: currentUser.username,
                        idVenueFoursquare: venue.id, created_at: new Date(), idS3asset: assetID)

                mCheckinManager.save(checkinCommand, onSuccessCreateCheckin(), onErrorCreateCheckin())
                break

            case UPDATE_CHECK_IN:
                checkinCommand = new CheckinCommand(method: method, state: state, origin: origin, price: price?.toString(), username: currentUser.username,
                        idVenueFoursquare: venue.id, created_at: currentCheckin.created_at, idS3asset: assetID)

                mCheckinManager.update(currentCheckin.id, checkinCommand, onSuccessUpdateCheckin(), onErrorUpdateCheckin())
                break
        }
    }

    private Closure onSuccessPhoto() {
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->
            idS3asset = response.body().id
            getFormCheckIn(idS3asset)
            loadingDialog.dismiss()
        }
    }

    private Closure onSuccessCreateCheckin() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 201) {
                getActivity().finish()
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onErrorCreateCheckin() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss()
        }
    }

    private Closure onSuccessUpdateCheckin() {
        { Call<Checkin> call, Response<Checkin> response ->
            Intent intent = new Intent()
            intent.putExtra(CURRENT_CHECK_IN, response.body())
            getActivity().setResult(Activity.RESULT_OK, intent)
            getActivity().finish()
        }
    }

    private Closure onErrorUpdateCheckin() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
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
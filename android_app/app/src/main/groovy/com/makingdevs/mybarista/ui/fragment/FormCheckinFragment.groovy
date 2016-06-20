package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.VenueCommand
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.service.FoursquareManager
import com.makingdevs.mybarista.service.FoursquareManagerImpl
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class FormCheckinFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "FormCheckinFragment"
    private EditText originEditText
    private EditText priceEditText
    private EditText noteEditText
    private Spinner methodFieldSprinner
    private Button checkInButton
    private static Context contextView
    private RatingBar ratingCoffe
    private Spinner venueSpinner

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    private Location mLastLocation
    private GoogleApiClient mGoogleApiClient
    private LocationRequest mLocationRequest
    private long UPDATE_INTERVAL = 10000
    private long FASTEST_INTERVAL = 2000

    List<Venue> venues

    FoursquareManager mFoursquareManager = FoursquareManagerImpl.instance

    FormCheckinFragment() {}

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_chek_in, container, false)
        originEditText = (EditText) root.findViewById(R.id.originField)
        priceEditText = (EditText) root.findViewById(R.id.priceField)
        noteEditText = (EditText) root.findViewById(R.id.noteField)
        methodFieldSprinner = (Spinner) root.findViewById(R.id.methodSpinner)
        checkInButton = (Button) root.findViewById(R.id.btnCheckIn);
        contextView = getActivity().getApplicationContext()
        ratingCoffe = (RatingBar) root.findViewById(R.id.rating_coffe_bar)
        venueSpinner = (Spinner) root.findViewById(R.id.spinner_venue)
        checkInButton.onClickListener = { saveCheckIn(getFormCheckIn()) }
        /*
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, new ArrayList<CharSequence>())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodFieldSprinner.adapter = adapter
        */
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCheckIn(getFormCheckIn())
            }
        });

        root
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                void onConnected(@Nullable Bundle bundle) {
                    Log.d(TAG, "GPS conectado....")
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                    if (mLastLocation != null) {
                        Log.d(TAG, "Ubicacion previa... " + mLastLocation.toString())
                    }
                    startLocationUpdates()
                }

                @Override
                void onConnectionSuspended(int i) {
                    if (i == CAUSE_SERVICE_DISCONNECTED) {
                        Log.d(TAG, "GPS desconectado....")
                    } else if (i == CAUSE_NETWORK_LOST) {
                        Log.d(TAG, "Conexion perdida....")
                    }
                }
            })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                @Override
                void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Log.d(TAG, "Error...." + connectionResult.errorMessage)
                }
            })
                    .addApi(LocationServices.API)
                    .build()
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setNumUpdates(1)

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    void onStart() {
        mGoogleApiClient.connect()
        super.onStart()
    }

    void onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect()
        }
    }


    private CheckinCommand getFormCheckIn() {
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String note = noteEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        String rating = ratingCoffe.getRating()
        User currentUser = mSessionManager.getUserSession(getContext())
        new CheckinCommand(method: method, note: note, origin: origin, price: price?.toString(), username: currentUser.username, rating: rating.toString())
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
        }
    }

    private Closure onSuccessGetVenues() {
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,"Venues... "+ response.body().dump().toString())
            venues = response.body() as List
            setVenuesToSpinner(venueSpinner,venues)
        }
    }

    private Closure onErrorGetVenues() {
        { Call<Checkin> call, Throwable t ->
            Log.d(TAG,"Error get venues... "+t.message)
        }
    }

    private void cleanForm() {
        originEditText.setText("")
        priceEditText.setText("")
        noteEditText.setText("")
    }

    void setVenuesToSpinner(Spinner spinner,List<Venue> venues){
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, venues.name)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter
    }

    @Override
    void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    void onConnectionSuspended(int i) {

    }

    @Override
    void onLocationChanged(Location location) {
        String latitude = location.getLatitude()
        String longitude = location.getLongitude()
        Log.d(TAG, "Ubicaion actual: $latitude $longitude")
        mFoursquareManager.getVenuesNear(new VenueCommand(latitude:latitude,longitude:longitude,query: "coffe"),onSuccessGetVenues(),onErrorGetVenues())
    }
}
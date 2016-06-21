package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.LocationUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.VenueCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

@CompileStatic
class FormCheckinFragment extends Fragment {

    static final String TAG = "FormCheckinFragment"
    static Context contextView
    EditText originEditText
    EditText priceEditText
    EditText noteEditText
    Spinner methodFieldSprinner
    Button checkInButton
    RatingBar ratingCoffe
    Spinner venueSpinner
    LocationUtil mLocationUtil
    Double latitude
    Double longitud

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance

    List<Venue> venues = [new Venue(name: "Selecciona lugar")]

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
        checkInButton.onClickListener =  { View v -> saveCheckIn(getFormCheckIn()) }
        Log.d(TAG, "${mLocationUtil.properties}")
        root
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        mLocationUtil = LocationUtil.newInstance()
        mLocationUtil.addPropertyChangeListener "latitude", { property -> this.latitude = property["newValue"] as Double }
        mLocationUtil.addPropertyChangeListener "longitude", { property -> this.longitud = property["newValue"] as Double }
        mLocationUtil.init(getActivity())
    }

    void onStart() {
        super.onStart()
        mLocationUtil.mGoogleApiClient.connect()
    }

    void onStop() {
        super.onStop()
        mLocationUtil.mGoogleApiClient.disconnect()
    }


    private CheckinCommand getFormCheckIn() {
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String note = noteEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        String rating = ratingCoffe.getRating()
        Integer selectIndexvenue = venueSpinner.getSelectedItemPosition()
        Venue detailVenue = getDetailVenueFromList(selectIndexvenue)
        User currentUser = mSessionManager.getUserSession(getContext())
        new CheckinCommand(method: method, note: note, origin: origin, price: price?.toString(), username: currentUser.username, rating: rating.toString(),idVenueFoursquare:detailVenue.id)
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
            //Log.d(TAG,"Venues... "+ response.body().dump().toString())
            venues.addAll(response.body() as List)
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

    Venue getDetailVenueFromList(Integer itemSelectedIndex){
        Venue detailVenue = venues.getAt(itemSelectedIndex)
    }

}
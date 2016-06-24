package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.LocationUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.GPSLocation
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

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
    GPSLocation mGPSLocation
    TextView addVenueButton
    List<Venue> venues = [new Venue(name: "Selecciona lugar")]
    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    SessionManager mSessionManager = SessionManagerImpl.instance
    FoursquareManager mFoursquareManager = FoursquareManagerImpl.instance
    String venueID

    // TODO: Refactor de nombres, diseño y responsabilidad
    LocationUtil mLocationUtil = LocationUtil.instance

    FormCheckinFragment() {}

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_chek_in, container, false)
        originEditText = (EditText) root.findViewById(R.id.originField)
        priceEditText = (EditText) root.findViewById(R.id.priceField)
        noteEditText = (EditText) root.findViewById(R.id.noteField)
        methodFieldSprinner = (Spinner) root.findViewById(R.id.methodSpinner)
        checkInButton = (Button) root.findViewById(R.id.btnCheckIn)
        addVenueButton = (TextView) root.findViewById(R.id.btnAddVenue)
        contextView = getActivity().getApplicationContext()
        ratingCoffe = (RatingBar) root.findViewById(R.id.rating_coffe_bar)
        checkInButton.onClickListener = { View v -> saveCheckIn(getFormCheckIn()) }
        addVenueButton.onClickListener = {
            callNewFragmentWithData(new SearchVenueFoursquareFragment())
            //SearchVenueFoursquareFragment searchVenueFoursquareFragmen = new SearchVenueFoursquareFragment()
        }
        root
    }

    private CheckinCommand getFormCheckIn() {
        String origin = originEditText.getText().toString()
        String price = priceEditText.getText().toString()
        String note = noteEditText.getText().toString()
        String method = methodFieldSprinner.getSelectedItem().toString()
        String rating = ratingCoffe.getRating()
        User currentUser = mSessionManager.getUserSession(getContext())
        new CheckinCommand(method: method, note: note, origin: origin, price: price?.toString(), username: currentUser.username, rating: rating.toString(), idVenueFoursquare: venueID, created_at:new Date())
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

    private void cleanForm() {
        originEditText.setText("")
        priceEditText.setText("")
        noteEditText.setText("")
    }

    void setVenuesToSpinner(Spinner spinner, List<Venue> venues) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, venues?.name)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter
    }

    @Override
    void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState)
        Bundle mBundle = new Bundle()
        mBundle = getArguments()
        if(mBundle){
            populateFormWithBundle(mBundle)
        }

    }

    void populateFormWithBundle(Bundle bundle){
        originEditText.text = bundle.getString("ORIGEN")
        priceEditText.text = bundle.getString("PRECIO")
        noteEditText.text = bundle.getString("NOTAS")
        methodFieldSprinner.setSelection(new Integer(bundle.getString("METODO")))
        ratingCoffe.rating = new Float(bundle.getString("RATING"))
        addVenueButton.text = bundle.getString("VENUE_NAME")
        venueID = bundle.getString("VENUE_ID")
    }

    void callNewFragmentWithData(Fragment fragment){
        Bundle bundle = new Bundle()
        bundle.putString("ORIGEN", originEditText.text.toString())
        bundle.putString("PRECIO", priceEditText.text.toString())
        bundle.putString("NOTAS", noteEditText.text.toString())
        bundle.putString("METODO", methodFieldSprinner.selectedItemPosition as String)
        bundle.putString("RATING", ratingCoffe.getRating() as String)
        FragmentTransaction ft = getFragmentManager().beginTransaction()
        fragment.setArguments(bundle)
        ft.replace(R.id.container,fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
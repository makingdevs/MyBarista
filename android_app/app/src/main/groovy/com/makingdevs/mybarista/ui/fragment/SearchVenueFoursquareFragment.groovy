package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.LocationUtil
import com.makingdevs.mybarista.model.GPSLocation
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.model.command.VenueCommand
import com.makingdevs.mybarista.service.FoursquareManager
import com.makingdevs.mybarista.service.FoursquareManagerImpl
import com.makingdevs.mybarista.ui.adapter.VenueAdapter
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
class SearchVenueFoursquareFragment extends Fragment {

    SearchVenueFoursquareFragment() {}

    private static final String TAG = "SearchVenueFoursquareFragment"

    // TODO: Refactor de nombres, diseño y responsabilidad
    LocationUtil mLocationUtil = LocationUtil.instance

    FoursquareManager mFoursquareManager = FoursquareManagerImpl.instance
    GPSLocation mGPSLocation
    String query
    String currentLatitude
    String currentLongitude
    List<Venue> venues = new ArrayList<Venue>()
    ImageButton searchVenueFoursquareButton
    EditText searchVenueFoursquareEditText
    RecyclerView mListVenues
    VenueAdapter mVenueAdapter
    Closure onVenueSelectedWithFragmentManagerControl

    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_venue_foursquare, container, false)
        searchVenueFoursquareButton = (ImageButton) root.findViewById(R.id.btnSearchVenueFoursquare)
        searchVenueFoursquareEditText = (EditText) root.findViewById(R.id.search_venue_foursquare)
        mListVenues = (RecyclerView) root.findViewById(R.id.list_venues_recicler_view)
        mListVenues.setLayoutManager(new LinearLayoutManager(getActivity()))
        searchVenueFoursquareButton.onClickListener = {
            if (currentLatitude) {
                query = searchVenueFoursquareEditText.text
                println("Foursquare..." + query + currentLatitude + currentLongitude)
                mFoursquareManager.getVenuesNear(new VenueCommand(latitude: currentLatitude, longitude: currentLongitude, query: query), onSuccessGetVenues(), onErrorGetVenues())
            } else {
                Log.d(TAG, "Huesos")
            }
        }
        root
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        mGPSLocation = new GPSLocation()
        mGPSLocation.addPropertyChangeListener { property ->
            GPSLocation gpsLocation = property["source"] as GPSLocation
            if (gpsLocation.latitude && gpsLocation.longitude) {
                mFoursquareManager.getVenuesNear(new VenueCommand(latitude: gpsLocation.latitude.toString(), longitude: gpsLocation.longitude.toString(), query: ""), onSuccessGetVenues(), onErrorGetVenues())
                currentLatitude = gpsLocation.latitude
                currentLongitude = gpsLocation.longitude
            }
        }
        // TODO: Refactor de nombres, diseño y responsabilidad
        // No sé si es un singleton, y si hay que inicializar con context y objeto al mismo tiempo
        mLocationUtil.init(getActivity(), mGPSLocation)
        //Log.d(TAG, "${mGPSLocation}")
    }

    void onStart() {
        super.onStart()
        mLocationUtil.mGoogleApiClient.connect()
        //Log.d(TAG, "${mGPSLocation}")
    }

    void onStop() {
        super.onStop()
        mLocationUtil.mGoogleApiClient.disconnect()
        //Log.d(TAG, "${mGPSLocation}")
    }

    private Closure onSuccessGetVenues() {
        { Call<Venue> call, Response<Venue> response ->
            //Log.d(TAG,"Venues... "+ response.body().dump().toString())
            venues.clear()
            venues.addAll(response.body() as List)

            if (!mVenueAdapter) {
                mVenueAdapter = new VenueAdapter(getContext(), venues)
                mVenueAdapter.onItemSelected = { Venue venue ->
                    onVenueSelectedWithFragmentManagerControl.call(venue)
                    fragmentManager.popBackStack()
                }
                mListVenues.adapter = mVenueAdapter
            } else {
                mVenueAdapter.setmVenues(venues)
                mVenueAdapter.notifyDataSetChanged()
            }
        }
    }

    private Closure onErrorGetVenues() {
        { Call<Venue> call, Throwable t ->
            Log.d(TAG, "Error get venues... " + t.message)
        }
    }
}
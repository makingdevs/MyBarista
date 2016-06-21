package com.makingdevs.mybarista.common

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.makingdevs.mybarista.model.GPSLocation
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
class LocationUtil implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = "LocationUtil"

    Location mLastLocation
    GoogleApiClient mGoogleApiClient
    LocationRequest mLocationRequest
    final long UPDATE_INTERVAL = 10000
    final long FASTEST_INTERVAL = 2000
    GPSLocation mGPSLocation

    void init(Context context, GPSLocation mGPSLocation){
        this.mGPSLocation = mGPSLocation
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
    }

    @Override
    void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (!mLastLocation) {
            mGPSLocation.setLatitude(mLastLocation.latitude)
            mGPSLocation.setLongitude(mLastLocation.longitude)
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

    @Override
    void onLocationChanged(Location location) {
        mGPSLocation.setLatitude(location.getLatitude())
        mGPSLocation.setLongitude(location.getLongitude())
    }

    @Override
    void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Error...." + connectionResult.errorMessage)
    }

    private void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
        //.setNumUpdates(1)
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }
}
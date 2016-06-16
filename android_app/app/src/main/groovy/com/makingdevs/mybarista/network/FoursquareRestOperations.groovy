package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Venue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

public interface FoursquareRestOperations {

    @GET("foursquare/searh_venues")
    Call<List<Venue>> getVenues(@QueryMap Map<String, String> foursquareQuery)

}
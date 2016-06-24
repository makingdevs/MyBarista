package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Venue
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

@CompileStatic
interface FoursquareRestOperations {

    @GET("foursquare/searh_venues")
    Call<List<Venue>> getVenues(@QueryMap Map<String, String> foursquareQuery)

}
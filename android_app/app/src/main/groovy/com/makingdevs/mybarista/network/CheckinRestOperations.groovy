package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.command.CheckinCommand
import retrofit2.Call
import retrofit2.http.*

public interface CheckinRestOperations {

    @GET("checkins")
    Call<List<Checkin>> getCheckins(@QueryMap Map<String, String> options)

    @POST("checkins")
    Call<Checkin> createCheckinForm(@Body CheckinCommand checkin)

    @GET("checkins/{id}")
    Call<Checkin> getCheckin(@Path("id") String id)



}

package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.command.CheckinCommand
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

public interface CheckinRestOperations {

    @GET("checkins")
    Call<List<Checkin>> getCheckins(@QueryMap Map<String, String> options)

    @POST("checkins")
    Call<Checkin> createCheckinForm(@Body CheckinCommand checkin)

    @GET("checkins/{id}")
    Call<Checkin> getCheckin(@Path("id") String id)

}

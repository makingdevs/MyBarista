package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.CircleFlavor
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.CircleFlavorCommand
import retrofit2.Call
import retrofit2.http.*

public interface CheckinRestOperations {

    @GET("checkins")
    Call<List<Checkin>> getCheckins(@QueryMap Map<String, String> options)

    @POST("checkins")
    Call<Checkin> createCheckinForm(@Body CheckinCommand checkin)

    @GET("checkins/{id}")
    Call<Checkin> getCheckin(@Path("id") String id)

    @POST("checkins/{id}/circleFlavor")
    Call<Checkin> createCircleFlavor(@Path("id") String id,@Body CircleFlavorCommand circleFlavorCommand)

    @GET("circles/{id}")
    Call<CircleFlavor> getCircleFlavor(@Path("id") String id)

    @POST("checkins/{id}")
    Call<Checkin> setRationgCoffee(@Path("id") String id, @Body String ratingValue)

}

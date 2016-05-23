package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Checkin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

public interface CheckinRestOperations {

    @GET("checkins")
    Call<List<Checkin>> getCheckins(@QueryMap Map<String, String> options)

    @FormUrlEncoded
    @POST("checkins")
    Call<Checkin> createCheckinForm(@Field("method") String method,
                                    @Field("note") String note,
                                    @Field("origin") String origin,
                                    @Field("price") String price,
                                    @Field("username") String username);

}

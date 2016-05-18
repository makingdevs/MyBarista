package  makingdevs.com.mybarista.service

import makingdevs.com.mybarista.model.Checkin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

public interface ApiService {

    @GET("checkins")
    Call<List<Checkin>> getCheckins()

    @FormUrlEncoded
    @POST("checkins")
    Call<Checkin> createCheckinForm(@Field("method") String method,
                                    @Field("note") String note,
                                    @Field("origin") String origin,
                                    @Field("price") String price);

}

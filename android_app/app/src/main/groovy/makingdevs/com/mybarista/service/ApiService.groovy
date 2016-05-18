package  makingdevs.com.mybarista.service

import makingdevs.com.mybarista.model.Checkin
import retrofit2.Call
import retrofit2.http.GET

public interface ApiService {
    @GET("checkins")
    Call<List<Checkin>> getCheckins()

}

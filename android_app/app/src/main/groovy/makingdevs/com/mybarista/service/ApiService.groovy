package  makingdevs.com.mybarista.service

import makingdevs.com.mybarista.model.Status
import retrofit2.Call
import retrofit2.http.GET

public interface ApiService {
    @GET("InvoiceDetail.groovy")
    Call<Status> getData()
}

package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

public interface UserRestOperations {

    @POST("users")
    Call<User> registrationUser(@Body RegistrationCommand registration)

    @GET("login/user")
    Call<User> loginUser(@QueryMap Map<String, String> login)

}
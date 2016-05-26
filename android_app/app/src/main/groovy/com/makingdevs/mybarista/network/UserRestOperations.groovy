package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

public interface UserRestOperations {
    @POST("users")
    Call<User> registrationUser(@Body RegistrationCommand registration)
}
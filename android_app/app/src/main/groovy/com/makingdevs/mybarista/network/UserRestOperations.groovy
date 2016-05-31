package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

public interface UserRestOperations {

    @POST("users")
    Call<User> registrationUser(@Body RegistrationCommand registration)

    @GET("login/user")
    Call<User> loginUser(@QueryMap Map<String, String> login)

    @GET("users/{id}")
    Call<UserProfile> getUser(@Path("id") String id)

    @PUT("users/{id}")
    Call<UserProfile> updateUser(@Path("id") String id, @Body UpdateUserCommand updateUserCommand)

}
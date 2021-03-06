package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.http.*

@CompileStatic
interface UserRestOperations {

    @POST("users")
    Call<User> registrationUser(@Body RegistrationCommand registration)

    @GET("login/user")
    Call<User> loginUser(@QueryMap Map<String, String> login)

    @GET("users/{id}")
    Call<UserProfile> getUser(@Path("id") String id)

    @PUT("users/{id}")
    Call<UserProfile> updateUser(@Path("id") String id, @Body UpdateUserCommand updateUserCommand)

    @GET("/search/users")
    Call<List<UserProfile>> search(@QueryMap Map<String, String> options)

}
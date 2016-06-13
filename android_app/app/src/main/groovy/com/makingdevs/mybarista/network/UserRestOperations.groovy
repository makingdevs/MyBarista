package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.UserProfile
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.model.command.UserCommand
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

public interface UserRestOperations {

    @POST("users")
    Call<User> registrationUser(@Body RegistrationCommand registration)

    @GET("login/user")
    Call<User> loginUser(@QueryMap Map<String, String> login)

    @GET("users/{id}")
    Call<UserProfile> getUser(@Path("id") String id)

    @PUT("users/{id}")
    Call<UserProfile> updateUser(@Path("id") String id, @Body UpdateUserCommand updateUserCommand)

    @Multipart
    @POST("/users/image/profile")
    Call<ResponseBody> uploadImage(@Part("user") RequestBody user, @Part MultipartBody.Part file)

    @GET("/users/photo/{id}")
    Call<PhotoCheckin> getPhotoCheckin(@Path("id") String id)

}
package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.PhotoBarista
import com.makingdevs.mybarista.model.PhotoCheckin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

public interface S3AssetRestOperations {

    @Multipart
    @POST("/checkin/photo/save")
    Call<PhotoCheckin> uploadImage(@Part("checkin") RequestBody checkin, @Part("user") RequestBody user, @Part MultipartBody.Part file)

    @Multipart
    @POST("/barista/photo/save_photo")
    Call<PhotoBarista> uploadImageBarista(@Part("barista") RequestBody barista, @Part MultipartBody.Part file)
}
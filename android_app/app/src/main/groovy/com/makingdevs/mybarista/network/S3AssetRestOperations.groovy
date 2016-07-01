package com.makingdevs.mybarista.network


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
    Call<PhotoCheckin> uploadImageBarista(@Part MultipartBody.Part file)

    @Multipart
    @POST("/users/photo/save")
    Call<PhotoCheckin> uploadImageProfile(@Part("user") RequestBody user,@Part MultipartBody.Part file)

    @GET("/s3asset/{id}")
    Call<PhotoCheckin> getS3(@Path("id") String id)

}
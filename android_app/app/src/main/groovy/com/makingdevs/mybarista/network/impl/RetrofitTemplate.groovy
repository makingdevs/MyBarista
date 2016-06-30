package com.makingdevs.mybarista.network.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.makingdevs.mybarista.BuildConfig
import com.makingdevs.mybarista.model.*
import com.makingdevs.mybarista.model.repository.UserRepository
import com.makingdevs.mybarista.network.*
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Singleton
@CompileStatic
class RetrofitTemplate {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.API_SERVER_URL)
            .build()

    def withRetrofit(Class operations, Closure onSuccess, Closure onError, Closure action){
        CheckinRestOperations restOperations = retrofit.create(operations) as CheckinRestOperations
        Call<Checkin> model = action(restOperations) as Call<Checkin>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Checkin>)
    }

    def withRetrofitUser(Class operations, Closure onSuccess, Closure onError, Closure action){
        UserRestOperations restOperations = retrofit.create(operations) as UserRestOperations
        Call<User> model = action(restOperations) as Call<User>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<User>)
    }

    def withRetrofitComemnt(Class operations, Closure onSuccess, Closure onError, Closure action){
        CommentRestOperations restOperations = retrofit.create(operations) as CommentRestOperations
        Call<Comment> model = action(restOperations) as Call<Comment>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Comment>)
    }

    def withRetrofitResponse(Class operations, Closure onSuccess, Closure onError, Closure action){
        S3AssetRestOperations restOperations = retrofit.create(operations) as S3AssetRestOperations
        Call<ResponseBody> model = action(restOperations) as Call<ResponseBody>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<ResponseBody>)
    }

    def withRetrofitPhotoCheckin(Class operations, Closure onSuccess, Closure onError, Closure action){
        S3AssetRestOperations restOperations = retrofit.create(operations) as S3AssetRestOperations
        Call<PhotoCheckin> model = action(restOperations) as Call<PhotoCheckin>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<PhotoCheckin>)
    }

    def withRetrofitBarista(Class operations, Closure onSuccess, Closure onError, Closure action){
        BaristaRestOperations restOperations = retrofit.create(operations) as BaristaRestOperations
        Call<Barista> model = action(restOperations) as Call<Barista>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Barista>)
    }

    def withRetrofitVenue(Class operations, Closure onSuccess, Closure onError, Closure action){
        FoursquareRestOperations restOperations = retrofit.create(operations) as FoursquareRestOperations
        Call<Venue> model = action(restOperations) as Call<Venue>
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Venue>)
    }
}
package com.makingdevs.mybarista.network.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.makingdevs.mybarista.BuildConfig
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.Comment
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.Venue
import com.makingdevs.mybarista.network.BaristaRestOperations
import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.CommentRestOperations
import com.makingdevs.mybarista.network.FoursquareRestOperations
import com.makingdevs.mybarista.network.UserRestOperations
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Singleton
class RetrofitTemplate {

    final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.API_SERVER_URL)
            .build()

    def withRetrofit(Class operations, Closure onSuccess, Closure onError, Closure action){
        CheckinRestOperations restOperations = retrofit.create(operations)
        Call<Checkin> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Checkin>)
    }

    def withRetrofitUser(Class operations, Closure onSuccess, Closure onError, Closure action){
        UserRestOperations restOperations = retrofit.create(operations)
        Call<User> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Checkin>)
    }

    def withRetrofitComemnt(Class operations, Closure onSuccess, Closure onError, Closure action){
        CommentRestOperations restOperations = retrofit.create(operations)
        Call<Comment> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Comment>)
    }

    def withRetrofitResponse(Class operations, Closure onSuccess, Closure onError, Closure action){
        UserRestOperations restOperations = retrofit.create(operations)
        Call<ResponseBody> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<ResponseBody>)
    }

    def withRetrofitPhotoCheckin(Class operations, Closure onSuccess, Closure onError, Closure action){
        UserRestOperations restOperations = retrofit.create(operations)
        Call<PhotoCheckin> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<PhotoCheckin>)
    }

    def withRetrofitBarista(Class operations, Closure onSuccess, Closure onError, Closure action){
        BaristaRestOperations restOperations = retrofit.create(operations)
        Call<Checkin> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<PhotoCheckin>)
    }

    def withRetrofitVenue(Class operations, Closure onSuccess, Closure onError, Closure action){
        FoursquareRestOperations restOperations = retrofit.create(operations)
        Call<Venue> model = action(restOperations)
        def callback = [
                onResponse :onSuccess,
                onFailure : onError
        ]
        model.enqueue(callback as Callback<Venue>)
    }
}
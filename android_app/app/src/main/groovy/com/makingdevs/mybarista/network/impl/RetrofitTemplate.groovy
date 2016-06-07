package com.makingdevs.mybarista.network.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.Comment
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.CommentRestOperations
import com.makingdevs.mybarista.network.UserRestOperations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Singleton
class RetrofitTemplate {

    final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl('http://192.168.1.220:3000/')
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
}
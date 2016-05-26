package com.makingdevs.mybarista.network.impl

import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.UserRestOperations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Singleton
class RetrofitTemplate {

    final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl('http://192.168.1.198:3000/')
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
}
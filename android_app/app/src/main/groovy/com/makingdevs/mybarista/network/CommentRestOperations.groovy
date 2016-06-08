package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Comment
import com.makingdevs.mybarista.model.command.CommentCommand
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

public interface CommentRestOperations {

    @GET("comments")
    Call<List<Comment>> getComments(@QueryMap Map<String, String> options)

    @POST("checkins")
    Call<Comment> createComment(@Body CommentCommand comment)

}
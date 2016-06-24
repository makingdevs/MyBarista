package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Comment
import com.makingdevs.mybarista.model.command.CommentCommand
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

@CompileStatic
interface CommentRestOperations {

    @GET("checkins/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") String id)

    @POST("comments")
    Call<Comment> createComment(@Body CommentCommand comment)

}
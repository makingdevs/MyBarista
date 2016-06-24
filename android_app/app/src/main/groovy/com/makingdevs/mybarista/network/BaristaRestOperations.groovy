package com.makingdevs.mybarista.network

import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.BaristaCommand
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.http.*

@CompileStatic
interface BaristaRestOperations {

    @POST("barista/{id}/save")
    Call<Checkin> registrationBarista(@Body BaristaCommand baristaCommand,@Path("id") String id)

}
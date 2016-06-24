package com.makingdevs.mybarista.service

import android.util.Log
import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.model.command.VenueCommand
import com.makingdevs.mybarista.network.FoursquareRestOperations
import com.makingdevs.mybarista.network.UserRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import groovy.transform.CompileStatic
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Singleton
@CompileStatic
class FoursquareManagerImpl implements FoursquareManager {

    static operations = FoursquareRestOperations

    @Override
    void getVenuesNear(VenueCommand venueCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitVenue(operations as Class, onSuccess, onError) { FoursquareRestOperations foursquareRestOperations ->
//            Log.d("FoursquareManagerImpl","Values..."+venueCommand.dump().toString())
            foursquareRestOperations.getVenues(["latitude":venueCommand.latitude,"longitude":venueCommand.longitude,"query":venueCommand.query])
        }
    }
}
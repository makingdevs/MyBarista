package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.VenueCommand
import groovy.transform.CompileStatic

@CompileStatic
interface FoursquareManager {

    void getVenuesNear(VenueCommand venueCommand, Closure onSuccess, Closure onError)

}
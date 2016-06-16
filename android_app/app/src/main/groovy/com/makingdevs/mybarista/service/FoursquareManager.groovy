package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.VenueCommand

interface FoursquareManager {

    void getVenuesNear(VenueCommand venueCommand, Closure onSuccess, Closure onError)

}
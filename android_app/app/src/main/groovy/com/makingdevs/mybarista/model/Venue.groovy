package com.makingdevs.mybarista.model

import groovy.transform.CompileStatic

@CompileStatic
class Venue {
    String id
    String name
    LocationVenue location

    class LocationVenue{
        List<String> formattedAddress
    }
}
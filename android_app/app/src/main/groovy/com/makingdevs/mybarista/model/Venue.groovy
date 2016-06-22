package com.makingdevs.mybarista.model

class Venue {
    String id
    String name
    LocationVenue location

    class LocationVenue{
        List<String> formattedAddress
    }
}
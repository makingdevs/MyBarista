package com.makingdevs.mybarista.model.command

import com.makingdevs.mybarista.model.Venue
import groovy.transform.CompileStatic

@CompileStatic
class CheckinCommand {

    String method
    String note
    String origin
    String state
    String price
    String username
    String rating
    String idVenueFoursquare
    Date created_at
    String idS3asset
    Venue venue
}
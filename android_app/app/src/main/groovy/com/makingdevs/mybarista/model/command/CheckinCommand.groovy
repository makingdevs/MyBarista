package  com.makingdevs.mybarista.model.command

import groovy.transform.CompileStatic

@CompileStatic
class CheckinCommand {

    String method
    String note
    String origin
    String price
    String username
    String rating
    String idVenueFoursquare
    Date created_at
    String idS3asset

}
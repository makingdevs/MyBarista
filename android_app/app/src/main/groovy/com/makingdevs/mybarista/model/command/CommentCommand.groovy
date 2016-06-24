package com.makingdevs.mybarista.model.command

import groovy.transform.CompileStatic

@CompileStatic
class CommentCommand {

    String body
    String checkin_id
    String username
    Date created_at
}
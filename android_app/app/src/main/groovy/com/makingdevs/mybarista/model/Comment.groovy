package com.makingdevs.mybarista.model

import groovy.transform.CompileStatic

@CompileStatic
class Comment {

    String body
    Date created_at
    Author user

    class Author{
        String username
    }

}
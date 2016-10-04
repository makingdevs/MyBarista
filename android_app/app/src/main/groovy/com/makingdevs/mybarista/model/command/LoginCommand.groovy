package com.makingdevs.mybarista.model.command

import groovy.transform.CompileStatic

@CompileStatic
class LoginCommand {

    String username
    String firstName
    String lastName
    String password
    String email
    String token

    Boolean validateCommand(){
        this.username ==~ /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/
    }
}
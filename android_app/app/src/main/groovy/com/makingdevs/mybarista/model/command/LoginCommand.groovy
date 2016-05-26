package com.makingdevs.mybarista.model.command

class LoginCommand {

    String username
    String password

    Boolean validateCommand(){
        this.username ==~ /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/
    }
}
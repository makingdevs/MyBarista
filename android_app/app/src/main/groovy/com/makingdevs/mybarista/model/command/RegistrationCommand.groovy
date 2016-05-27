package com.makingdevs.mybarista.model

class RegistrationCommand{

    String username
    String password
    String confirmPassword

    Boolean validateCommand(){
        def pattern = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/
        this.username ==~ pattern && this.password == this.confirmPassword && this.username != this.password
    }

}
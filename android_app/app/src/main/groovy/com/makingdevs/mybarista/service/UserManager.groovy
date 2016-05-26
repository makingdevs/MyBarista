package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.LoginCommand

interface UserManager {

    void save(RegistrationCommand registrationCommand, Closure onSuccess, Closure onError)
    void login(LoginCommand loginCommand, Closure onSuccess, Closure onError)

}
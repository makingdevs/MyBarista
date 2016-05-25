package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand

interface UserManager {
    void save(RegistrationCommand registrationCommand, Closure onSuccess, Closure onError)
}
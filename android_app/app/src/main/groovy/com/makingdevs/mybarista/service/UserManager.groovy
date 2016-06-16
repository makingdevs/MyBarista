package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.model.command.UserCommand

interface UserManager {

    void save(RegistrationCommand registrationCommand, Closure onSuccess, Closure onError)
    void login(LoginCommand loginCommand, Closure onSuccess, Closure onError)
    void update(UpdateUserCommand updateUserCommand, Closure onSuccess, Closure onError)
    void getUser(String id, Closure onSuccess, Closure onError)
    void upload(UploadCommand uploadCommand, Closure onSuccess, Closure onError)
    void getPhoto(String idCheckin, Closure onSuccess, Closure onError)
    void seachUsers(Map options, Closure onSuccess, Closure onError)

}
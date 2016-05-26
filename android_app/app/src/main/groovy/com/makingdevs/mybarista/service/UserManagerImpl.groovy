package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.network.UserRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate


@Singleton
class UserManagerImpl implements UserManager {

    static operations = UserRestOperations

    @Override
    void save(RegistrationCommand registrationCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.registrationUser(registrationCommand)
        }
    }

    @Override
    void login(LoginCommand loginCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.loginUser([username:loginCommand.username,password:loginCommand.password])
        }
    }
}
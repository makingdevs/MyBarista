package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.network.UserRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import groovy.transform.CompileStatic

@Singleton
@CompileStatic
class UserManagerImpl implements UserManager {

    static operations = UserRestOperations

    @Override
    void save(RegistrationCommand registrationCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations as Class, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.registrationUser(registrationCommand)
        }
    }

    @Override
    void login(LoginCommand loginCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations as Class, onSuccess, onError) { UserRestOperations restOperations ->
            if (loginCommand.email) {
                restOperations.loginUser([username  : loginCommand.username
                                          , password: loginCommand.password
                                          , email   : loginCommand.email
                                          , token   : loginCommand.token
                ])
            } else {
                restOperations.loginUser([username  : loginCommand.username
                                          , password: loginCommand.password
                ])
            }
        }
    }

    @Override
    void update(UpdateUserCommand updateUserCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations as Class, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.updateUser(updateUserCommand.id, updateUserCommand)
        }
    }

    @Override
    void getUser(String id, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations as Class, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.getUser(id)
        }
    }

    @Override
    void seachUsers(Map options, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations as Class, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.search(options)
        }
    }

}
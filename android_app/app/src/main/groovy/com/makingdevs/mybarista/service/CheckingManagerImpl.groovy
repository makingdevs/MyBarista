package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate

@Singleton
class CheckingManagerImpl implements CheckinManager {

    static operations = CheckinRestOperations

    @Override
    void save(CheckinCommand checkin, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.createCheckinForm(checkin)
        }
    }

    @Override
    void list(Map params, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.getCheckins([username:"neodevelop"])
        }
    }

    @Override
    void show(String id, Closure onSuccess, Closure onError){
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.getCheckin(id)
        }
    }
}
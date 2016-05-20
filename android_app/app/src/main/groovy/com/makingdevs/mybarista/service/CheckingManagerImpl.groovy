package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate

@Singleton
class CheckingManagerImpl implements CheckinManager {

    static operations = CheckinRestOperations

    @Override
    void save(Map params, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.createCheckinForm(params.method,params.note, params.origin, params.price,"neodevelop")
        }
    }
}
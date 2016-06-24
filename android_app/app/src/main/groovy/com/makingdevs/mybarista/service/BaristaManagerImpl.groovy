package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.BaristaCommand
import com.makingdevs.mybarista.network.BaristaRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import groovy.transform.CompileStatic

@Singleton
@CompileStatic
class BaristaManagerImpl implements BaristaManager {

    static operations = BaristaRestOperations

    @Override
    void save(BaristaCommand baristaCommand,String CheckinId, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitBarista(operations as Class, onSuccess, onError) { BaristaRestOperations restOperations ->
            restOperations.registrationBarista(baristaCommand,CheckinId)
        }
    }
}
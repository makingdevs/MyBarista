package com.makingdevs.mybarista.service

import android.util.Log
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.CircleFlavorCommand
import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import com.makingdevs.mybarista.ui.fragment.CircleFlavorFragment

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
            restOperations.getCheckins(params)
        }
    }

    @Override
    void show(String id, Closure onSuccess, Closure onError){
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.getCheckin(id)
        }
    }

    @Override
    void saveCircle(String id,CircleFlavorCommand circleFlavorCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.createCircleFlavor(id,circleFlavorCommand)
        }
    }

    @Override
    void showCircleFlavor(String id, Closure onSuccess, Closure onError){
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.getCircleFlavor(id)
        }
    }

    @Override
    void  saveRating(String id, CheckinCommand command, Closure onSuccess,Closure onError){
        RetrofitTemplate.instance.withRetrofit(operations, onSuccess, onError){ CheckinRestOperations restOperations ->
            restOperations.setRationgCoffee(id,command)
        }
    }
}

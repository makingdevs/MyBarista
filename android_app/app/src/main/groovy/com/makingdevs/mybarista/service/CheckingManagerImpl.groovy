package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.CircleFlavorCommand
import com.makingdevs.mybarista.network.CheckinRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import groovy.transform.CompileStatic

@Singleton
@CompileStatic
class CheckingManagerImpl implements CheckinManager {

    static operations = CheckinRestOperations

    @Override
    void save(CheckinCommand checkin, Closure onSuccess, Closure onError) {

        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.createCheckinForm(checkin)
        }
    }

    @Override
    void list(Map params, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.getCheckins(params)
        }
    }

    @Override
    void show(String id, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.getCheckin(id)
        }
    }

    @Override
    void update(String id, CheckinCommand checkinCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.updateCheckin(id, checkinCommand)
        }
    }

    @Override
    void saveCircle(String id, CircleFlavorCommand circleFlavorCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.createCircleFlavor(id, circleFlavorCommand)
        }
    }

    @Override
    void showCircleFlavor(String id, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.getCircleFlavor(id)
        }
    }

    @Override
    void saveRating(String id, CheckinCommand command, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSuccess, onError) { CheckinRestOperations restOperations ->
            restOperations.setRationgCoffee(id, command)
        }
    }

    @Override
    void saveNote(String id, CheckinCommand checkinCommand, Closure onSucces, Closure onError) {
        RetrofitTemplate.instance.withRetrofit(operations as Class, onSucces, onError) { CheckinRestOperations restOperations ->
            restOperations.setNoteInCheckin(id, checkinCommand)
        }
    }
}

package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.CircleFlavorCommand
import groovy.transform.CompileStatic

@CompileStatic
interface CheckinManager {
    void save(CheckinCommand checkin, Closure onSuccess, Closure onError)
    void list(Map params, Closure onSuccess, Closure onError)
    void show(String id, Closure onSuccess, Closure onError)
    void saveCircle(String id,CircleFlavorCommand circleFlavorCommand,Closure onSuccess, Closure onError)
    void showCircleFlavor(String id, Closure onSuccess, Closure onError)
    void saveRating(String id, CheckinCommand checkinCommand, Closure onSuccess,Closure onError)
}
package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.CircleFlavorCommand

interface CheckinManager {
    void save(CheckinCommand checkin, Closure onSuccess, Closure onError)
    void list(Map params, Closure onSuccess, Closure onError)
    void show(String id, Closure onSuccess, Closure onError)
    void saveCircle(CircleFlavorCommand circleFlavorCommand,Closure onSuccess, Closure onError)
}
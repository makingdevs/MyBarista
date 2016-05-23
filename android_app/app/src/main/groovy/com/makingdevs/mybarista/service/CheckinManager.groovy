package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CheckinCommand

interface CheckinManager {
    void save(CheckinCommand checkin, Closure onSuccess, Closure onError)
    void list(Map params, Closure onSuccess, Closure onError)
}
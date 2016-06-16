package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.BaristaCommand

interface BaristaManager {

    void save(BaristaCommand baristaCommand,String id, Closure onSuccess, Closure onError)
}
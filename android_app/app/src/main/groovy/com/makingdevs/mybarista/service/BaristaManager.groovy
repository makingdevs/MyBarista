package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.BaristaCommand
import groovy.transform.CompileStatic

@CompileStatic
interface BaristaManager {

    void save(BaristaCommand baristaCommand,String id, Closure onSuccess, Closure onError)
    void show(String id, Closure onSuccess, Closure onError)

}
package com.makingdevs.mybarista.service

interface CheckinManager {
    void save(Map params, Closure onSuccess, Closure onError)
}
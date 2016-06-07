package com.makingdevs.mybarista.service

interface CommentManager {

    void list(Map params, Closure onSuccess, Closure onError)
}
package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CommentCommand

interface CommentManager {

    void list(String idCheckin, Closure onSuccess, Closure onError)
    void save(CommentCommand comment, Closure onSuccess, Closure onError)

}
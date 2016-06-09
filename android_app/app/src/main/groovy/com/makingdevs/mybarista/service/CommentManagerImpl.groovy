package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.CommentCommand
import com.makingdevs.mybarista.network.CommentRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate

@Singleton
class CommentManagerImpl implements CommentManager {

    static operations = CommentRestOperations

    @Override
    void list(String idCheckin, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitComemnt(operations, onSuccess, onError){ CommentRestOperations restOperations ->
            restOperations.getComments(idCheckin)
        }
    }

    @Override
    void save(CommentCommand comment, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitComemnt(operations, onSuccess, onError){ CommentRestOperations restOperations ->
            restOperations.createComment(comment)
        }
    }

}

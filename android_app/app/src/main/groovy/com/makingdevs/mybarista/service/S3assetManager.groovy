package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.UploadCommand

interface S3assetManager {

    void upload(UploadCommand uploadCommand, Closure onSuccess, Closure onError)

}
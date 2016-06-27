package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.model.command.UploadPhotoBaristaCommand

interface S3assetManager {

    void upload(UploadCommand uploadCommand, Closure onSuccess, Closure onError)
    void uploadPhotoBarista(UploadPhotoBaristaCommand uploadPhotoBaristaCommand, Closure onSuccess, Closure onError)

}
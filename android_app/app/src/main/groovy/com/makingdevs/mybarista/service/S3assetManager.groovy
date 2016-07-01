package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.UploadCommand

interface S3assetManager {

    void upload(UploadCommand uploadCommand, Closure onSuccess, Closure onError)
    void uploadPhotoBarista(UploadCommand uploadCommand, Closure onSuccess, Closure onError)
    void uploadPhotoUser(UploadCommand uploadCommand, Closure onSuccess, Closure onError)
    void getAsset(String id, Closure onSuccess, Closure onError)

}
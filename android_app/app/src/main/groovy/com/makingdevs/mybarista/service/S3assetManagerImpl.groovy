package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.network.S3AssetRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Singleton
class S3assetManagerImpl implements S3assetManager {

    static operations = S3AssetRestOperations

    @Override
    void upload(UploadCommand uploadCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitResponse(operations, onSuccess, onError) { S3AssetRestOperations restOperations ->

            File photoCheckinToUpload = new File(uploadCommand.pathFile)
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoCheckinToUpload)
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoCheckinToUpload.getName(), requestFile)

            RequestBody currentUSer = RequestBody.create(MediaType.parse("multipart/form-data"), uploadCommand.idUser)

            RequestBody currentCheckin = RequestBody.create(MediaType.parse("multipart/form-data"),uploadCommand.idCheckin )

            restOperations.uploadImage(currentCheckin,currentUSer,body)
        }
    }

    @Override
    void uploadPhoto(UploadCommand uploadCommand, Closure onSuccess, Closure onError){
        RetrofitTemplate.instance.withRetrofitResponse(operations, onSuccess, onError) { S3AssetRestOperations restOperations ->

            File photoToUpload = new File(uploadCommand.pathFile)
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoToUpload)
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoToUpload.getName(), requestFile)

            restOperations.uploadImageBarista(body)
        }
    }
    @Override
    void uploadPhotoUser(UploadCommand uploadCommand, Closure onSuccess, Closure onError){
        RetrofitTemplate.instance.withRetrofitResponse(operations, onSuccess, onError) { S3AssetRestOperations restOperations ->

            File photoToUpload = new File(uploadCommand.pathFile)
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoToUpload)
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoToUpload.getName(), requestFile)

            RequestBody user = RequestBody.create(MediaType.parse("multipart/form-data"), uploadCommand.idUser)

            restOperations.uploadImageProfile(user,body)
        }
    }

    @Override
    void getAsset(String id, Closure onSuccess, Closure onError) {
       RetrofitTemplate.instance.withRetrofitResponse(operations as Class, onSuccess, onError) { S3AssetRestOperations restOperations ->
                restOperations.getS3(id)
       }
    }
}
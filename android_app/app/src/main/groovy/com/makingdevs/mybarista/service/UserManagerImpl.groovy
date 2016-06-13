package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.CheckinCommand
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.model.command.UpdateUserCommand
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.model.command.UserCommand
import com.makingdevs.mybarista.network.UserRestOperations
import com.makingdevs.mybarista.network.impl.RetrofitTemplate
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Singleton
class UserManagerImpl implements UserManager {

    static operations = UserRestOperations

    @Override
    void save(RegistrationCommand registrationCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.registrationUser(registrationCommand)
        }
    }

    @Override
    void login(LoginCommand loginCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.loginUser([username:loginCommand.username,password:loginCommand.password])
        }
    }

    @Override
    void update(UpdateUserCommand updateUserCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.updateUser(updateUserCommand.id,updateUserCommand)
        }
    }

    @Override
    void getUser(String id, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitUser(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.getUser(id)
        }
    }

    @Override
    void upload(UploadCommand uploadCommand, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitResponse(operations, onSuccess, onError) { UserRestOperations restOperations ->

            File photoCheckinToUpload = new File(uploadCommand.pathFile)
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoCheckinToUpload)
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoCheckinToUpload.getName(), requestFile)

            RequestBody currentUSer = RequestBody.create(MediaType.parse("multipart/form-data"), uploadCommand.idUser)

            RequestBody currentCheckin = RequestBody.create(MediaType.parse("multipart/form-data"),uploadCommand.idCheckin )

            restOperations.uploadImage(currentCheckin,currentUSer,body)
        }
    }

    @Override
    void getPhoto(String idUser,String idCheckin, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitPhotoCheckin(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.getPhotoCheckin(idUser,idCheckin)
        }
    }

}
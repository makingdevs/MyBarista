package com.makingdevs.mybarista.service

import com.makingdevs.mybarista.model.RegistrationCommand
import com.makingdevs.mybarista.model.command.LoginCommand
import com.makingdevs.mybarista.model.command.UpdateUserCommand
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
    void upload(UserCommand user,String uriFile, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitResponse(operations, onSuccess, onError) { UserRestOperations restOperations ->

            File photoCheckinToUpload = new File(uriFile)
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoCheckinToUpload)
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoCheckinToUpload.getName(), requestFile)

            RequestBody currentUSer = RequestBody.create(MediaType.parse("multipart/form-data"), user.id)

            restOperations.uploadImage(currentUSer,body)
        }
    }

    @Override
    void getPhoto(String id, Closure onSuccess, Closure onError) {
        RetrofitTemplate.instance.withRetrofitString(operations, onSuccess, onError) { UserRestOperations restOperations ->
            restOperations.getPhotoCheckin(id)
        }
    }

}
package com.makingdevs.mybarista.service

import android.content.Context
import android.content.SharedPreferences
import com.makingdevs.mybarista.model.User

@Singleton
class SessionManagerImpl implements SessionManager{

    void setUserSession(User user, Context context) {
        SharedPreferences.Editor session = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit()
        session.putString("username",user.username)
        session.putString("token",user.token)
        session.commit()
    }

    User getUserSession(Context context) {
        SharedPreferences session = context.getSharedPreferences("UserSession",Context.MODE_PRIVATE)
        String username = session.getString("username",null);
        String token = session.getString("token",null)
        new User(username:username,token:token)
    }

    Boolean isUserSession(Context context){
        SharedPreferences session = context.getSharedPreferences("UserSession",Context.MODE_PRIVATE)
        String username = session.getString("username",null);
        username != null
    }

    void setLogout(Context context){
        SharedPreferences.Editor session = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit()
        session.remove("username")
        session.remove("token")
        session.commit()
    }
}
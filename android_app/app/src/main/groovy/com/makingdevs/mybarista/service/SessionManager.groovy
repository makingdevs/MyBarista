package com.makingdevs.mybarista.service

import android.content.Context
import com.makingdevs.mybarista.model.User

interface SessionManager {

    void setUserSession(User user, Context context)
    Map getUserSession(Context context)
}
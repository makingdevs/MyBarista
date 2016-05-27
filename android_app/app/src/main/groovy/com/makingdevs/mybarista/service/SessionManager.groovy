package com.makingdevs.mybarista.service

import android.content.Context
import com.makingdevs.mybarista.model.User

interface SessionManager {

    void setUserSession(User user, Context context)
    User getUserSession(Context context)
    Boolean isUserSession(Context context)
    void setLogout(Context context)

}
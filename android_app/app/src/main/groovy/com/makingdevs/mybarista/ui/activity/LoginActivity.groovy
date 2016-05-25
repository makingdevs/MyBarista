package com.makingdevs.mybarista.ui.activity

import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.LoginFragment
import groovy.transform.CompileStatic

@CompileStatic
class LoginActivity extends SingleFragmentActivity{

    @Override
    Fragment createFragment() {
        new LoginFragment()
    }
}
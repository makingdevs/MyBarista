package com.makingdevs.mybarista.ui.activity

import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.RegistrationFragment
import groovy.transform.CompileStatic

@CompileStatic
class RegistrationActivity extends SingleFragmentActivity{

    @Override
    Fragment createFragment() {
        new RegistrationFragment()
    }
}
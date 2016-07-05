package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.RegistrationFragment
import groovy.transform.CompileStatic

@CompileStatic
class RegistrationActivity extends SingleFragmentActivity{

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, RegistrationActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        //TODO: cambiar a meotodo newInstance
        new RegistrationFragment()
    }
}
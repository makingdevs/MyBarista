package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.BaristaFragment
import groovy.transform.CompileStatic

@CompileStatic
class BaristaActivity extends SingleFragmentActivity{

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, BaristaActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new BaristaFragment()
    }
}
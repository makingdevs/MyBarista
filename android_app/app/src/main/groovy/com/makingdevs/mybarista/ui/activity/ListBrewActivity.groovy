package com.makingdevs.mybarista.ui.activity

import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.ListBrewFragment
import groovy.transform.CompileStatic

@CompileStatic
public class ListBrewActivity extends SingleFragmentActivity {

    @Override
    Fragment createFragment() {
        new ListBrewFragment()
    }
}

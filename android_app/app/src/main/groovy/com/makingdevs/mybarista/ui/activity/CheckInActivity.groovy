package com.makingdevs.mybarista.ui.activity

import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.FormCheckinFragment
import groovy.transform.CompileStatic

@CompileStatic
public class CheckinActivity extends SingleFragmentActivity {

    @Override
    Fragment createFragment() {
        new FormCheckinFragment()
    }

}
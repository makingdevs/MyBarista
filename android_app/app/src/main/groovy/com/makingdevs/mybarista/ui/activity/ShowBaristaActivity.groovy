package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.ShowBaristaFragment
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import groovy.transform.CompileStatic

@CompileStatic
class ShowBaristaActivity extends SingleFragmentActivity{

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, ShowBaristaActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        //TODO: cambiar a meotodo newInstance
        new ShowBaristaFragment()
    }
}
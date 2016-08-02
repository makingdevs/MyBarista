package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.FormCheckinFragment
import groovy.transform.CompileStatic

@CompileStatic
public class CheckInActivity extends SingleFragmentActivity {

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, CheckInActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        //TODO: cambiar a meotodo newInstance
        new FormCheckinFragment()
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
    }
}
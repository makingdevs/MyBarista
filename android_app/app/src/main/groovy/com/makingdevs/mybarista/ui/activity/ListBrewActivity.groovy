package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.ListBrewFragment
import groovy.transform.CompileStatic

@CompileStatic
public class ListBrewActivity extends SingleFragmentActivity {

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, ListBrewActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new ListBrewFragment()
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        createMenu()
    }
}

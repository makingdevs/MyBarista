package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import groovy.transform.CompileStatic

@CompileStatic
public class ShowCheckinActivity extends SingleFragmentActivity {

    static String EXTRA_CHECKIN_ID = "checkin_id"

    static Intent newIntentWithContext(Context context, String id){
        Intent intent = new Intent(context, ShowCheckinActivity)
        intent.putExtra(EXTRA_CHECKIN_ID, id)
        intent
    }

    @Override
    Fragment createFragment() {
        String id = getIntent()?.extras.getSerializable(EXTRA_CHECKIN_ID)
        if(!id) throw new IllegalArgumentException("El checkin no tiene ID, mmmm tamales!")
        new ShowCheckinFragment(id)
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        createMenu()
    }
}
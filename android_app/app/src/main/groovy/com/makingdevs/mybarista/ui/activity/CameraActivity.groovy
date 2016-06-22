package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.CameraFragment

class CameraActivity extends SingleFragmentActivity{

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, CameraActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new CameraFragment()
    }
}
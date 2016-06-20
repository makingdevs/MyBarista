package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.ProfileFragment

class ProfileActivity extends SingleFragmentActivity{

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, ProfileActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new ProfileFragment()
    }
}
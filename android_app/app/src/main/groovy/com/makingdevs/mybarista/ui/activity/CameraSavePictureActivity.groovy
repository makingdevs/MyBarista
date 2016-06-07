package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.CameraSavePictureFragment

class CameraSavePictureActivity extends SingleFragmentActivity{

    private static final String TAG = "CameraSavePcictureActivity"

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, CameraSavePictureActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new CameraSavePictureFragment()
    }

    Bundle getUriFile(){
        Bundle extras = getIntent()?.getExtras()
    }

}
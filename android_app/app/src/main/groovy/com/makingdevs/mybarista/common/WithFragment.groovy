package com.makingdevs.mybarista.common

import android.support.v4.app.Fragment
import groovy.transform.CompileStatic

@CompileStatic
trait WithFragment {
    abstract Fragment createFragment()
    abstract void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
}
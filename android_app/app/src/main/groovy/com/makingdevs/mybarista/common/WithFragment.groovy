package com.makingdevs.mybarista.common

import android.support.v4.app.Fragment

trait WithFragment {
    abstract Fragment createFragment()
}
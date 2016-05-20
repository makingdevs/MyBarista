package com.makingdevs.mybarista.common

import android.support.v4.app.Fragment

/**
 * Created by makingdevs on 20/05/16.
 */
trait WithFragment {
    abstract Fragment createFragment()
}
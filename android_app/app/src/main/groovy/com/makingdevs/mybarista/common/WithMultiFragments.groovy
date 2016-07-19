package com.makingdevs.mybarista.common

import groovy.transform.CompileStatic

@CompileStatic
trait WithMultiFragments {
    abstract Map createFragments()
    abstract void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
}
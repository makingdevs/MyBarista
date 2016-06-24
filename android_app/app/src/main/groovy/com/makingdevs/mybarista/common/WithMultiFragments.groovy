package com.makingdevs.mybarista.common

import groovy.transform.CompileStatic

@CompileStatic
trait WithMultiFragments {
    abstract Map createFragments()
}
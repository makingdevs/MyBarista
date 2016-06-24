package com.makingdevs.mybarista.model

import groovy.beans.Bindable
import groovy.transform.CompileStatic

@CompileStatic
class GPSLocation {
    @Bindable
    Double latitude
    @Bindable
    Double longitude
}
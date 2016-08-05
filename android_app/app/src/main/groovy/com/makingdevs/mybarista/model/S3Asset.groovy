package com.makingdevs.mybarista.model

import groovy.beans.Bindable
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@EqualsAndHashCode
class S3Asset implements Serializable {

    String id
    @Bindable
    String url_file

}

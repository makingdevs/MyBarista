package com.makingdevs.mybarista.model

import groovy.transform.CompileStatic

@CompileStatic
class Barista implements Serializable {

    String name
    String id
    S3Asset s3_asset = new S3Asset()

}
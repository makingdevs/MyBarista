package  com.makingdevs.mybarista.model

import groovy.transform.CompileStatic

@CompileStatic
class Barista{

    String name
    String id
    S3Asset s3_asset = new S3Asset()

}
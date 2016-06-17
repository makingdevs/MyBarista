package  com.makingdevs.mybarista.model

class Checkin {

    Date created_at
    String id
    String method
    String note
    String origin
    String price

    Date updated_at
    String user_id
    String circle_flavor_id
    String rating
    S3_asset s3_asset
    Barista baristum

    class S3_asset{
        String url_file
    }

    class Barista{
        String name
        String id
    }
}
module Models exposing (..)


type alias Model =
  { id : Int
  , name : Maybe String
  , username : String
  , s3_asset : Maybe S3Asset
  , checkins : List Checkin
  , checkins_count : Int
  }

type alias Checkin =
    { author : String
    , id : Int
    , s3_asset : Maybe CheckinS3Asset
    }

type alias CheckinS3Asset =
    { id : Int
    , url_file : String }

type alias S3Asset =
    { url_file : String }

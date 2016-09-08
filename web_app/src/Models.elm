module Models exposing (..)


type alias Model =
  { id : Int
  , name : Maybe String
  , username : String
  , s3_asset : Maybe UserS3Asset
  , checkins : List Checkin
  , checkins_count : Int
  , show_checkin : Maybe Bool
  }

type alias UserS3Asset =
    { url_file : String }


type alias Checkin =
    { author : String
    , id : Int
    , s3_asset : Maybe CheckinS3Asset
    , show_checkin : Maybe Bool
    }

type alias CheckinS3Asset =
    { id : Int
    , url_file : String }



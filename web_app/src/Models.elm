module Models exposing (..)


type alias Model =
  { id : Int
  , name : Maybe String
  , username : String
  , s3_asset : Maybe UserS3Asset
  , checkins : List Checkin
  , checkins_count : Int
  }

type alias UserS3Asset =
    { url_file : String }


type alias Checkin =
    { author : String
    , id : Int
    , s3_asset : Maybe CheckinS3Asset
    , comments : List CheckinComment
    , show_checkin : Maybe Bool
    }

type alias CheckinS3Asset =
    { id : Int
    , url_file : String }

type alias CheckinComment =
    { body : String
    , created_at : String
    , user : Friend }

type alias Friend =
    { username : String }



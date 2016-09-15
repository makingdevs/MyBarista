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
    { id : Int
    , author : String
    , note : Maybe String
    , rating : Maybe String
    , s3_asset : Maybe CheckinS3Asset
    , venue : Maybe Venue
    , comments : List CheckinComment
    , show_checkin : Maybe Bool
    }

type alias CheckinS3Asset =
    { id : Int
    , url_file : String }

type alias CheckinComment =
    { body : String
    , created_at : String
    , user : CommentAuthor }

type alias CommentAuthor =
    { username : String }

type alias Venue =
    { name : String }



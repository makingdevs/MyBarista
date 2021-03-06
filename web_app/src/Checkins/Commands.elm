
module Checkins.Commands exposing (..)

import Models exposing (..)
import Messages exposing (Msg(..))
import Api exposing (checkinUrl, checkinsUrl)
import Json.Decode as Decode exposing ((:=))
import Task exposing (Task)
import Http


fetchCheckinsCmd : String -> Cmd Msg
fetchCheckinsCmd username =
    Http.get checkinsDecoder (checkinsUrl username)
        |> Task.perform FetchCheckinsError FetchCheckinsSuccess

fetchCheckinCmd : Int -> Cmd Msg
fetchCheckinCmd id =
    Http.get checkinDecoder (checkinUrl id)
        |> Task.perform FetchCheckinError FetchCheckinSuccess


-- DECODERS

checkinsDecoder : Decode.Decoder (List Checkin)
checkinsDecoder =
    Decode.list checkinDecoder

checkinDecoder : Decode.Decoder Checkin
checkinDecoder =
    Decode.object8 Checkin
        ("id" := Decode.int)
        ("author" := Decode.string)
        (Decode.maybe("note" := Decode.string))
        (Decode.maybe("rating" := Decode.string))
        (Decode.maybe("s3_asset" := s3AssetDecoder))
        (Decode.maybe("venue" := venueDecoder))
        ("comments" := commentsDecoder)
        (Decode.maybe("show_checkin" := Decode.bool))

s3AssetDecoder : Decode.Decoder CheckinS3Asset
s3AssetDecoder =
    Decode.object2 CheckinS3Asset
        ("id" := Decode.int)
        ("url_file" := Decode.string)

commentsDecoder : Decode.Decoder (List CheckinComment)
commentsDecoder =
    Decode.list commentDecoder

commentDecoder : Decode.Decoder CheckinComment
commentDecoder =
    Decode.object3 CheckinComment
        ( "body" := Decode.string )
        ( "created_at" := Decode.string )
        ( "user" := commentAuthorDecoder )

commentAuthorDecoder : Decode.Decoder CommentAuthor
commentAuthorDecoder =
    Decode.object1 CommentAuthor
        ( "username" := Decode.string )

venueDecoder : Decode.Decoder Venue
venueDecoder =
    Decode.object1 Venue
        ("name" := Decode.string)

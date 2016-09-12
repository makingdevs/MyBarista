
module Checkins.Commands exposing (..)

import Models exposing (Checkin, CheckinS3Asset, CheckinComment, CommentAuthor)
import Messages exposing (Msg(..))
import Api exposing (checkinUrl)
import Json.Decode as Decode exposing ((:=))
import Task exposing (Task)
import Http


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
    Decode.object5 Checkin
        ("author" := Decode.string)
        ("id" := Decode.int)
        (Decode.maybe("s3_asset" := s3AssetDecoder))
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

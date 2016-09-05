
module Checkins.Commands exposing (..)

import Models exposing (Checkin, CheckinS3Asset)
import Json.Decode as Decode exposing ((:=))


checkinsDecoder : Decode.Decoder (List Checkin)
checkinsDecoder =
    Decode.list checkinDecoder

checkinDecoder : Decode.Decoder Checkin
checkinDecoder =
    Decode.object3 Checkin
        ("author" := Decode.string)
        ("id" := Decode.int)
        (Decode.maybe("s3_asset" := s3AssetDecoder))

s3AssetDecoder : Decode.Decoder CheckinS3Asset
s3AssetDecoder =
    Decode.object2 CheckinS3Asset
        ("id" := Decode.int)
        ("url_file" := Decode.string)

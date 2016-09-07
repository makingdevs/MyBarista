
module Users.Commands exposing (..)

import Models exposing (Model, UserS3Asset)
import Messages exposing (Msg(..))
import Checkins.Commands exposing (checkinsDecoder)
import Api exposing (user)
import Http
import Task exposing (Task)
import Json.Decode as Decode exposing ((:=))


fetchUserCmd : String -> Cmd Msg
fetchUserCmd username =
    Http.get userDecoder (user ++ username)
        |> Task.perform FetchUserError FetchUserSuccess


-- DECODERS

userDecoder : Decode.Decoder Model
userDecoder =
    Decode.object6 Model
        ("id" := Decode.int)
        (Decode.maybe("name" := Decode.string))
        ("username" := Decode.string)
        (Decode.maybe("s3_asset" := s3AssetDecoder))
        ("checkins" := checkinsDecoder)
        ("checkins_count" := Decode.int)

s3AssetDecoder : Decode.Decoder UserS3Asset
s3AssetDecoder =
    Decode.object1 UserS3Asset
        ("url_file" := Decode.string)

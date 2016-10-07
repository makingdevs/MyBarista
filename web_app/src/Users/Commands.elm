
module Users.Commands exposing (..)

import Models exposing (User, UserS3Asset)
import Messages exposing (Msg(..))
import Checkins.Commands exposing (checkinsDecoder)
import Api exposing (userUrl)
import Http
import Task exposing (Task)
import Json.Decode as Decode exposing ((:=))


fetchUserCmd : String -> Cmd Msg
fetchUserCmd username =
    Http.get userDecoder (userUrl username)
        |> Task.perform FetchUserError FetchUserSuccess


-- DECODERS

userDecoder : Decode.Decoder User
userDecoder =
    Decode.object5 User
        ("id" := Decode.int)
        (Decode.maybe("name" := Decode.string))
        ("username" := Decode.string)
        (Decode.maybe("s3_asset" := s3AssetDecoder))
        ("checkins_count" := Decode.int)

s3AssetDecoder : Decode.Decoder UserS3Asset
s3AssetDecoder =
    Decode.object1 UserS3Asset
        ("url_file" := Decode.string)

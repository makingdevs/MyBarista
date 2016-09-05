
module Main exposing (..)

-- Elm Core
import Models exposing (..)
import Messages exposing (Msg(..))
import Update exposing (update)
import View exposing (view)
import Http
import Task exposing (Task)
import Json.Decode as Decode exposing ((:=))
import Navigation
import UrlParser exposing (..)
import String

{- Routing -}
-- Ruta deseada: /user/username
type UserProfile
    = MyBarista
    | UserRoute String

matchers : Parser ( UserProfile -> a ) a
matchers =
           oneOf
               {- El orden de los matchers importa -}
               [ format MyBarista ( UrlParser.s "" )
               , format UserRoute ( UrlParser.s "user" </> string ) ]

hashParser : Navigation.Location -> Result String UserProfile
hashParser location =
    location.hash
        |> String.dropLeft 2
        |> parse identity matchers

parser : Navigation.Parser (Result String UserProfile)
parser =
    Navigation.makeParser hashParser

urlUpdate : Result String UserProfile -> Model -> (Model, Cmd Msg)
urlUpdate result model =
    case result of
       Ok route ->
           case route of
               MyBarista ->
                   ( model, Cmd.none )
               UserRoute username ->
                   ( model, fetchUserCmd username)
       Err error ->
           ( model, Cmd.none )


init : Result String UserProfile -> (Model, Cmd Msg)
init result =
    urlUpdate result ( { id = 0
                         , name = Nothing
                         , username = ""
                         , s3_asset = Just { url_file = "http://barist.coffee.s3.amazonaws.com/avatar.png" }
                         , checkins = []
                         , checkins_count = 0 }
                       )

-- UPDATE

-- Base url
api : String
api =
    "http://mybarista.makingdevs.com/"

-- User endpoint
userUrl : String
userUrl =
    api ++ "user/"

-- User command
fetchUserCmd : String -> Cmd Msg
fetchUserCmd username =
    Http.get userDecoder (userUrl ++ username)
        |> Task.perform FetchUserError FetchUserSuccess

-- User decoder
userDecoder : Decode.Decoder Model
userDecoder =
    Decode.object6 Model
        ("id" := Decode.int)
        (Decode.maybe("name" := Decode.string))
        ("username" := Decode.string)
        (Decode.maybe("s3_asset" := s3AssetDecoder))
        ("checkins" := checkinsDecoder)
        ("checkins_count" := Decode.int)

-- Checkin S3Asset decoder
checkinAssetDecoder : Decode.Decoder CheckinS3Asset
checkinAssetDecoder =
    Decode.object2 CheckinS3Asset
        ("id" := Decode.int)
        ("url_file" := Decode.string)

-- S3Asset decoder
s3AssetDecoder : Decode.Decoder S3Asset
s3AssetDecoder =
    Decode.object1 S3Asset
        ("url_file" := Decode.string)

-- Checkin and List Checkin decoder
checkinsDecoder : Decode.Decoder (List Checkin)
checkinsDecoder =
    Decode.list checkinDecoder

checkinDecoder : Decode.Decoder Checkin
checkinDecoder =
    Decode.object3 Checkin
        ("author" := Decode.string)
        ("id" := Decode.int)
        (Decode.maybe("s3_asset" := checkinAssetDecoder))


-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none


main : Program Never
main =
  Navigation.program parser
    { init = init
    , update = update
    , view = view
    , urlUpdate = urlUpdate
    , subscriptions = subscriptions
    }

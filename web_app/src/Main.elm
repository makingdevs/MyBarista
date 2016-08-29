
module Main exposing (..)

-- Elm Core
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import Task exposing (Task)
import Json.Decode as Decode exposing ((:=))
import Navigation
import UrlParser exposing (..)
import String

{- Routing -}
-- Ruta deseada: /user/username
matchers : Parser ( Msg -> a ) a
matchers =
           oneOf
               {- El orden de los matchers importa -}
               [ format MyBarista ( UrlParser.s "" )
               , format User ( UrlParser.s "user" </> string ) ]

hashParser : Navigation.Location -> Result String Msg
hashParser location =
    location.hash
        |> String.dropLeft 2
        |> parse identity matchers

parser : Navigation.Parser (Result String Msg)
parser =
    Navigation.makeParser hashParser

urlUpdate : Result String Msg -> Model -> (Model, Cmd Msg)
urlUpdate result model =
    case result of
       Ok route ->
           ( { model | username = toString route }, Cmd.none)
       Err error ->
           ( { model | username = toString error }, Cmd.none)

-- MODEL


type alias Checkin =
    { author : String
    , id : Int
    , s3_asset : CheckinS3Asset
    }

type alias CheckinS3Asset =
    { id : Int
    , url_file : String }

type alias S3Asset =
    { url_file : String }

type alias Model =
  { id : Int
  , name : String
  , username : String
  , s3_asset : S3Asset
  , checkins : List Checkin
  , checkins_count : Int
  }

init : Result String Msg -> (Model, Cmd Msg)
init result =
    urlUpdate result ( { id = 0
                         , name = ""
                         , username = ""
                         , s3_asset = { url_file = "http://barist.coffee.s3.amazonaws.com/avatar.png" }
                         , checkins = []
                         , checkins_count = 0 }
                       )

-- UPDATE


-- Base url
api : String
api =
    "http://192.168.1.21:3000/"

-- User endpoint
userUrl : String
userUrl =
    api ++ "users/1"

-- User command
fetchUserCmd : Cmd Msg
fetchUserCmd =
    Http.get userDecoder userUrl
        |> Task.perform FetchUserError FetchUserSuccess

-- User decoder
userDecoder : Decode.Decoder Model
userDecoder =
    Decode.object6 Model
        ("id" := Decode.int)
        ("name" := Decode.string)
        ("username" := Decode.string)
        ("s3_asset" := s3AssetDecoder)
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
        ("s3_asset" := checkinAssetDecoder)


type Msg
  = MyBarista
    | User String
    | UserNotFound
    | FetchUserSuccess Model
    | FetchUserError Http.Error


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    MyBarista ->
        ( model, Cmd.none )
    User username ->
        ( { model | username = username }, fetchUserCmd )
    UserNotFound ->
        ( model, Cmd.none )
    FetchUserSuccess user ->
        ( { model | username = user.username
          , s3_asset = user.s3_asset
          , checkins = user.checkins
          , checkins_count = user.checkins_count
          }
        , Cmd.none)
    FetchUserError error ->
        ( {model | username = toString error}, Cmd.none)


-- CHILD VIEWS

-- Navigation
navigation : Html.Html Msg
navigation =
    div[ class "navigation-main row" ]
        [ div [ class "navigation__brand col-xs-6 col-sm-4 col-md-4" ]
              [ div [ class "navigation__brand-container" ]
                    [ img [ src "http://barist.coffee.s3.amazonaws.com/ic_barista_logo.png", class "navigation__image"] []
                    ]
              , div [ class "navigation__text col-sm-4 col-md-4" ]
                    [ p []
                          [ text "Barista"]
                    ]
              ]
        , div [ class "navigation__search-box hidden-xs col-sm-4 col-md-4" ]
              [ input [ type' "text"
                      , placeholder "Buscar"
--                      , onInput SearchUser
                      ] []
              ]
        , div [ class "navigation__href-session col-xs-6 col-sm-4 col-md-4" ]
              [ ul [ class "navigation__session-items"]
                    [ li [ class "navigation__session-item"] [ text "Descarga la applicación" ]
                    ]
              ]
        ]

-- Profile
profile : Model -> Html.Html Msg
profile model =
    div [ class "profile-page row" ]
        [ div [ class "profile-page__header"]
              [ div [ class "profile-page__author-container row" ]
                    [ div [ class "profile-page__avatar-container col-xs-4 col-sm-4 col-md-4" ]
                          [ img [ src model.s3_asset.url_file, class "profile-page__avatar img-circle" ] []
                          ]
                    , div [ class "profile-page__user-info col-xs-8 col-sm-8 col-md-8" ]
                          [ div [ class "profile-page__username" ]
                                [ p []
                                      [ text model.username ]
                                ]
                          , div [ class "profile-page__statistics" ]
                                [ p []
                                      [ text ( (toString model.checkins_count) ++ " checkins") ]
                                ]
                          ]
                    ]
              ]
        ]

-- Grid
renderCheckin : Checkin -> Html.Html Msg
renderCheckin checkin =
  li [ class "post-grid__item"] [ img [ class "post-grid__item-image", src <| checkin.s3_asset.url_file] [] ]

renderCheckins : List Checkin -> Html.Html Msg
renderCheckins checkins =
  let
    items = List.map renderCheckin checkins
  in
    ul [ class "post-grid__items"] items

grid : Model -> Html.Html Msg
grid model =
    div [ class "post-grid" ]
        [ div [ class "post-grid__items-container"]
              [ renderCheckins model.checkins ]
        ]

-- Footer
footer : Html.Html Msg
footer =
    div [ class "footer-main" ]
        [ div [ class "footer-nav" ]
              [ ul [ class "footer__nav-items"]
                    [ li [ class "footer__nav-item" ] [ text "Enlace 1" ]
                    , li [ class "footer__nav-item" ] [ text "Enlace 2" ]
                    , li [ class "footer__nav-item col" ] [ text "Enlace 3" ]
                    ]
              ]
        ]

-- VIEW


view : Model -> Html Msg
view model =
  div [ class "shell" ]
      [ div [class "shell__nav"]
            [ navigation
            ]
      , div [class "shell__content"]
            [ profile model
            , grid model
            ]
      , div [class "shell__footer"]
            [ footer
            ]
      ]

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

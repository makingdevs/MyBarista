
module Main exposing (..)

import Models exposing (..)
import Messages exposing (Msg(..))
import Update exposing (update)
import View exposing (view)
import Routing exposing (..)
import Users.Commands exposing (..)
import Navigation


init : Result String Route -> (Model, Cmd Msg)
init result =
    let
        placeholder =  "http://barist.coffee.s3.amazonaws.com/avatar.png"
    in
        urlUpdate result ( { id = 0
                           , name = Nothing
                           , username = ""
                           , s3_asset = Just { url_file = placeholder }
                           , checkins = []
                           , checkins_count = 0
                           , show_checkin = Just False }
                         )


subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none


urlUpdate : Result String Route -> Model -> (Model, Cmd Msg)
urlUpdate result model =
    case result of
       Ok route ->
           case route of
               Home ->
                   ( model, Cmd.none )
               UserProfile username ->
                   ( model, fetchUserCmd username)
       Err error ->
           ( model, Cmd.none )


main : Program Never
main =
  Navigation.program parser
    { init = init
    , update = update
    , view = view
    , urlUpdate = urlUpdate
    , subscriptions = subscriptions
    }

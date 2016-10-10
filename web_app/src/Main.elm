
module Main exposing (..)

import Models exposing (..)
import Messages exposing (Msg(..))
import Update exposing (update)
import View exposing (view)
import Routing exposing (..)
import Users.Commands exposing (..)
import Checkins.Commands exposing (fetchCheckinCmd)
import Navigation exposing (..)


initModel : Model
initModel =
    let
        placeholder =  "http://barist.coffee.s3.amazonaws.com/avatar.png"
    in
        { user = { id = 0
                 , name = Nothing
                 , username = ""
                 , s3_asset = Just { url_file = placeholder }
                 , checkins_count = 0
                 }
        , checkins = []
        , currentPage = NotFound
        }

init : Result String Page -> (Model, Cmd Msg)
init result =
    urlUpdate result (initModel)

subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none


urlUpdate : Result String Page -> Model -> (Model, Cmd Msg)
urlUpdate result model =
    case result of
        Ok page ->
            case page of
                ProfilePage username ->
                    ({model | currentPage = page}, fetchUserCmd username)

                CheckinPage username id ->
                    ({model | currentPage = page}, fetchCheckinCmd id)

                NotFound ->
                    ({model | currentPage = page}, Cmd.none)
        Err _ ->
            (initModel, modifyUrl (toHash model.currentPage))

main : Program Never
main =
  Navigation.program parser
    { init = init
    , update = update
    , view = view
    , urlUpdate = urlUpdate
    , subscriptions = subscriptions
    }

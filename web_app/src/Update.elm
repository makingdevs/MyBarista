
module Update exposing (..)

import Models exposing (Model, Checkin)
import Messages exposing (Msg(..))
import Users.Commands exposing (fetchUserCmd)
import Checkins.Commands exposing (fetchCheckinCmd)


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/coffee.jpg"
    in
        case msg of
            FetchUserSuccess user ->
                ( { model | username = user.username
                  , s3_asset = user.s3_asset
                  , checkins = List.reverse user.checkins
                  , checkins_count = user.checkins_count
                  }
                , Cmd.none )
            FetchUserError error ->
                ( {model | username = "User not found :("
                  , s3_asset = Nothing
                  , checkins = []
                  , checkins_count = 0
                  }
                , Cmd.none )
            ShowCheckin checkin ->
                ( showDialog model checkin.id
                , fetchCheckinCmd checkin.id )
            HideCheckin checkin ->
                ( cancelDialog model checkin.id
                , Cmd.none )
            FetchCheckinSuccess checkin ->
                ( showDialog model checkin.id
                , Cmd.none )
            FetchCheckinError error ->
                ( model, Cmd.none )


showDialog : Model -> Int -> Model
showDialog model id =
    let
        newCheckins =
            List.map
                (\checkin ->
                     if checkin.id == id then
                         { checkin
                             | show_checkin = Just True
                         }
                     else
                         checkin
                )
                model.checkins
    in
        { model
            | checkins = newCheckins
        }

cancelDialog : Model -> Int -> Model
cancelDialog model id =
    let
        newCheckins =
            List.map
                (\checkin ->
                     if checkin.id == id then
                         { checkin
                             | show_checkin = Just False
                         }
                     else
                         checkin
                )
                model.checkins
     in
         { model
             | checkins = newCheckins
         }


module Update exposing (..)

import Models exposing (Model, Checkin, User)
import Messages exposing (Msg(..))
import Checkins.Commands exposing (fetchCheckinCmd, fetchCheckinsCmd)
import Routing exposing (..)
import Navigation exposing (..)


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/coffee.jpg"
    in
        case msg of
            Navigate page ->
                (model, newUrl (toHash page))

            FetchUserSuccess user ->
                (showUser model user, fetchCheckinsCmd user.username)

            FetchUserError error ->
                (model, modifyUrl (toHash NotFound))

            FetchCheckinsSuccess checkins ->
                ({ model
                      | checkins = checkins }, Cmd.none)

            FetchCheckinsError error ->
                (model, modifyUrl (toHash NotFound))

            ShowCheckin checkin ->
                {- Modify Url -}
                (showDialog model checkin, Cmd.none)

            HideCheckin checkin ->
                (cancelDialog model checkin.id, back 0)

            FetchCheckinSuccess checkin ->
                Debug.log "Data: "
                    (showDialog model checkin, Cmd.none)

            FetchCheckinError error ->
                (model, modifyUrl (toHash model.currentPage))

showUser : Model -> User -> Model
showUser model user =
    let
        newUser =
            { user
                | username = user.username
                , s3_asset = user.s3_asset
                , checkins_count = user.checkins_count
            }
    in
        { model
            | user = newUser }

showDialog : Model -> Checkin -> Model
showDialog model checkinSelected =
    let
        newCheckins =
            List.map
                (\checkin ->
                     if checkin.id == checkinSelected.id then
                         { checkin
                             | note = checkinSelected.note
                             , rating = checkinSelected.rating
                             , venue = checkinSelected.venue
                             , show_checkin = Just True
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

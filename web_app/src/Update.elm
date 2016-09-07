
module Update exposing (..)

import Models exposing (Model)
import Messages exposing (Msg(..))


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    FetchUserSuccess user ->
        ( { model | username = user.username
          , s3_asset = user.s3_asset
          , checkins = List.reverse user.checkins
          , checkins_count = user.checkins_count
          }
        , Cmd.none)
    FetchUserError error ->
        ( {model | username = "User not found :("
          , s3_asset = Nothing
          , checkins = []
          , checkins_count = 0
          }
        , Cmd.none)
    ShowCheckinDialog checkin ->
        Debug.log ("Checkin clicked! " ++ ( toString checkin.id ) )
            ( { model
                  | show_checkin = Just True }
            , Cmd.none )
    CancelCheckinDialog ->
       ( { model
             | show_checkin = Just False }
       , Cmd.none )

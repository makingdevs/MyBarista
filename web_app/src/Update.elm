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


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
                  | show_checkin = Just True
                  , checkins =
                    [ { author = checkin.author
                      , id = checkin.id
                      , s3_asset = Just { id = checkin.s3_asset
                                               |> Maybe.map .id
                                               |> Maybe.withDefault 0
                                        , url_file =  checkin.s3_asset
                                                      |> Maybe.map .url_file
                                                      |> Maybe.withDefault ""
                                        }
                      , show_checkin = Just True
                      }
                    ]
              }
            , Cmd.none )
    CancelCheckinDialog checkin ->
       ( { model
             | show_checkin = Just False
             , checkins =
               [ { author = checkin.author
                 , id = checkin.id
                 , s3_asset = Just { id = checkin.s3_asset
                                          |> Maybe.map .id
                                          |> Maybe.withDefault 0
                                   , url_file = checkin.s3_asset
                                                |> Maybe.map .url_file
                                                |> Maybe.withDefault ""
                                   }
                 , show_checkin = Just False
                 }
               ]
         }
       , Cmd.none )

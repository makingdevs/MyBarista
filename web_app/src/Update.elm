
module Update exposing (..)

import Models exposing (Model)
import Messages exposing (Msg(..))
import Users.Commands exposing (fetchUserCmd)
import Checkins.Commands exposing (fetchCommentsCmd)


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
            ShowCheckinDialog checkin ->
                Debug.log ("Checkin clicked! " ++ ( toString checkin.id ) )
                    ( { model
                          | checkins =
                            [ { author = checkin.author
                              , id = checkin.id
                              , s3_asset = Just { id = checkin.s3_asset
                                                |> Maybe.map .id
                                                |> Maybe.withDefault 0
                                                , url_file =  checkin.s3_asset
                                                |> Maybe.map .url_file
                                                |> Maybe.withDefault placeholder
                                                }
                              , comments = Just []
                              , show_checkin = Just True
                              }
                            ]
                      }
                    , fetchCommentsCmd checkin.id )
            CancelCheckinDialog checkin ->
                Debug.log "Closed"
                    ( model
                    , fetchUserCmd model.username )
            FetchCommentsSuccess comments ->
                Debug.log (" Comments: :D " ++ (toString comments))
                    ( { model
                          | checkins =
                            [ { comments = comments } ]
                      }
                    , Cmd.none )
            FetchCommentsError error ->
                ( model, Cmd.none )

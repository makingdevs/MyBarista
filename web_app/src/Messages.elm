module Messages exposing (..)

import Models exposing (User, Checkin)
import Http


type Msg
  = FetchUserSuccess User
    | FetchUserError Http.Error
    | FetchCheckinsSuccess (List Checkin)
    | FetchCheckinsError Http.Error
    | ShowCheckin Checkin
    | HideCheckin Checkin
    | FetchCheckinSuccess Checkin
    | FetchCheckinError Http.Error

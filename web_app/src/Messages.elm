module Messages exposing (..)

import Models exposing (User, Checkin)
import Routing exposing (Page)
import Http


type Msg
  = Navigate Page
    | FetchUserSuccess User
    | FetchUserError Http.Error
    | FetchCheckinsSuccess (List Checkin)
    | FetchCheckinsError Http.Error
    | ShowCheckin Checkin
    | HideCheckin Checkin
    | FetchCheckinSuccess Checkin
    | FetchCheckinError Http.Error

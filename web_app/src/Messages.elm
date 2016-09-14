module Messages exposing (..)

import Models exposing (Model, Checkin)
import Http


type Msg
  = FetchUserSuccess Model
    | FetchUserError Http.Error
    | ShowCheckin Checkin
    | HideCheckin Checkin
    | FetchCheckinSuccess Checkin
    | FetchCheckinError Http.Error

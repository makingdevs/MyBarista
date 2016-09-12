module Messages exposing (..)

import Models exposing (Model, Checkin, CheckinComment)
import Http


type Msg
  = FetchUserSuccess Model
    | FetchUserError Http.Error
    | ShowCheckinDialog Checkin
    | CancelCheckinDialog Checkin
    | FetchCheckinSuccess Checkin
    | FetchCheckinError Http.Error

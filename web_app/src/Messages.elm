module Messages exposing (..)

import Models exposing (Model)
import Http


type Msg
  = FetchUserSuccess Model
    | FetchUserError Http.Error

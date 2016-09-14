
module Api exposing (..)


baseUrl : String
baseUrl =
    "http://192.168.1.244:3000/"

userUrl : String -> String
userUrl username =
    baseUrl ++ "user/" ++ username

checkinUrl : Int -> String
checkinUrl id =
    baseUrl ++ "checkins/" ++ (toString id)

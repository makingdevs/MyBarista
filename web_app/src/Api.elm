
module Api exposing (..)


baseUrl : String
baseUrl =
    "http://mybarista.makingdevs.com/"

userUrl : String -> String
userUrl username =
    baseUrl ++ "user/" ++ username

checkinUrl : Int -> String
checkinUrl id =
    baseUrl ++ "checkins/" ++ (toString id)

checkinsUrl : String -> String
checkinsUrl username =
    baseUrl ++ "checkins/" ++ "?username=" ++ username

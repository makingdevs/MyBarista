
module Api exposing (..)


baseUrl : String
baseUrl =
    "http://mybarista.makingdevs.com/"

userUrl : String -> String
userUrl username =
    baseUrl ++ "user/" ++ username

commentsUrl : Int -> String
commentsUrl id =
    baseUrl ++ "checkins/" ++ (toString id) ++ "/comments"

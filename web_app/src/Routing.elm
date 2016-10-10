
module Routing exposing (..)

import Navigation
import UrlParser exposing (..)
import String exposing (..)


type Page
    = Home
    | ProfilePage String
    | CheckinPage String Int
    | NotFound

{- The parser does not work with CheckinPage -}
matchers : Parser ( Page -> a ) a
matchers =
           oneOf
               {- El orden de los matchers importa -}
               [ format Home ( s "" )
               , format ProfilePage ( s "profile" </> string )
               , format CheckinPage ( s "profile" </> string </> s "checkin" </> int )
               , format NotFound ( s "not-found" )
               ]

hashParser : Navigation.Location -> Result String Page
hashParser location =
    Debug.log ("Location" ++ (toString location))
        location.hash
            |> String.dropLeft 1
            |> parse identity matchers

parser : Navigation.Parser (Result String Page)
parser =
    Navigation.makeParser hashParser


toHash : Page -> String
toHash page =
    case page of
        Home ->
            "#"
        ProfilePage username ->
            "#profile/" ++ username
        CheckinPage username id ->
            concat
                [ "#profile/"
                , toLower username
                , "/checkin/"
                , toString id
                ]
        NotFound ->
            "#not-found"

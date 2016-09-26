module Routing exposing (..)

import Navigation
import UrlParser exposing (..)
import String


type Page
    = Home
    | ProfilePage String
    | CheckinPage Int

matchers : Parser ( Page -> a ) a
matchers =
           oneOf
               {- El orden de los matchers importa -}
               [ format Home ( s "" )
               , format ProfilePage ( s "#profile" </> string )
               , format CheckinPage ( s "#checkin" </> int) ]

hashParser : Navigation.Location -> Result String Page
hashParser location =
    location.hash
        |> String.dropLeft 0
        |> parse identity matchers

parser : Navigation.Parser (Result String Page)
parser =
    Navigation.makeParser hashParser

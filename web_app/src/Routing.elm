module Routing exposing (..)

import Navigation
import UrlParser exposing (..)
import String


type Route
    = Home
    | UserProfile String

matchers : Parser ( Route -> a ) a
matchers =
           oneOf
               {- El orden de los matchers importa -}
               [ format Home ( s "" )
               , format UserProfile ( s "user" </> string ) ]

hashParser : Navigation.Location -> Result String Route
hashParser location =
    location.hash
        |> String.dropLeft 2
        |> parse identity matchers

parser : Navigation.Parser (Result String Route)
parser =
    Navigation.makeParser hashParser

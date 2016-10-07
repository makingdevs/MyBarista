module Routing exposing (..)

import Navigation
import UrlParser exposing (..)
import String


type Page
    = Home
    | UserProfile String

matchers : Parser ( Page -> a ) a
matchers =
           oneOf
               {- El orden de los matchers importa -}
               [ format Home ( s "" )
               , format UserProfile ( s "user" </> string ) ]

hashParser : Navigation.Location -> Result String Page
hashParser location =
    location.hash
        |> String.dropLeft 2
        |> parse identity matchers

parser : Navigation.Parser (Result String Page)
parser =
    Navigation.makeParser hashParser

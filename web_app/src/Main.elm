
module Main exposing (..)

-- Elm Core
import Html exposing (..)
import Html.App
import Html.Attributes exposing (..)
import Html.Events exposing (..)


-- MODEL

type alias Checkin =
    { s3_asset : String
    , author : String
    }

type alias Model =
  { name : String
  , username : String
  , s3_asset : String
  , checkins : List Checkin
  , checkins_count : Int
  }

init : (Model, Cmd Msg)
init =
  (
   Model
       "User"
       "@username"
       "http://barist.coffee.s3.amazonaws.com/avatar.png"
       []
       120
  , Cmd.none
  )


-- UPDATE


type Msg
  = NoOp
    | SearchUser


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    NoOp ->
      (model, Cmd.none)
    SearchUser ->
        ({model | name = "Ariana"}, Cmd.none)

-- CHILD VIEWS

-- Navigation
navigation : Html.Html Msg
navigation =
    div[ class "navigation-main row" ]
        [ div [ class "navigation__brand col-xs-6 col-sm-4 col-md-4" ]
              [ div [ class "navigation__brand-container" ]
                    [ img [ src "http://barist.coffee.s3.amazonaws.com/ic_barista_logo.png", class "navigation__image"] []
                    ]
              , div [ class "navigation__text col-sm-4 col-md-4" ]
                    [ p []
                          [ text "Barista"]
                    ]
              ]
        , div [ class "navigation__search-box hidden-xs col-sm-4 col-md-4" ]
              [ input [ type' "text"
                      , placeholder "Buscar"
                      ] []
              ]
        , div [ class "navigation__href-session col-xs-6 col-sm-4 col-md-4" ]
              [ ul [ class "navigation__session-items"]
                    [ li [ class "navigation__session-item"] [ text "Descarga la applicación" ]
                    ]
              ]
        ]

-- Profile
profile : Model -> Html.Html Msg
profile model =
    div [ class "profile-page row" ]
        [ div [ class "profile-page__header"]
              [ div [ class "profile-page__author-container row" ]
                    [ div [ class "profile-page__avatar-container col-xs-4 col-sm-4 col-md-4" ]
                          [ img [ src model.s3_asset, class "profile-page__avatar img-circle" ] []
                          ]
                    , div [ class "profile-page__user-info col-xs-8 col-sm-8 col-md-8" ]
                          [ div [ class "profile-page__username" ]
                                [ p []
                                      [ text model.username ]
                                ]
                          , div [ class "profile-page__statistics" ]
                                [ p []
                                      [ text ( (toString model.checkins_count) ++ " checkins") ]
                                ]
                          ]
                    ]
              ]
        ]

-- Grid
renderCheckin : Checkin -> Html.Html Msg
renderCheckin checkin =
  li [ class "post-grid__item"] [ text <| checkin.s3_asset ]

renderCheckins : List Checkin -> Html.Html Msg
renderCheckins checkins =
  let
    items = List.map renderCheckin checkins
  in
    ul [ class "post-grid__items"] items

grid : Model -> Html.Html Msg
grid model =
    div [ class "post-grid" ]
        [ div [ class "post-grid__items-container"]
              [ renderCheckins model.checkins ]
        , div [ class "post-grid__get-items" ]
              [ button []
                       [ text "Cargar más" ]
              ]
        ]

-- Footer
footer : Html.Html Msg
footer =
    div [ class "footer-main" ]
        [ div [ class "footer-nav" ]
              [ ul [ class "footer__nav-items"]
                    [ li [ class "footer__nav-item" ] [ text "Enlace 1" ]
                    , li [ class "footer__nav-item" ] [ text "Enlace 2" ]
                    , li [ class "footer__nav-item col" ] [ text "Enlace 3" ]
                    ]
              ]
        ]

-- VIEW


view : Model -> Html Msg
view model =
  div [ class "shell" ]
      [ div [class "shell__nav"]
            [ navigation
            ]
      , div [class "shell__content"]
            [ profile model
            , grid model
            ]
      , div [class "shell__footer"]
            [ footer
            ]
      ]

-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none


main : Program Never
main =
  Html.App.program
    { init = init
    , update = update
    , view = view
    , subscriptions = subscriptions
    }

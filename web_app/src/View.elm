
module View exposing (..)

import Models exposing (Model, Checkin)
import Messages exposing (Msg(..))
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Dialog


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


-- Navigation
navigation : Html.Html Msg
navigation =
    div[ class "navigation-main row" ]
        [ div [ class "navigation__brand col-xs-6 col-sm-6 col-md-6" ]
              [ div [ class "navigation__brand-container" ]
                    [ img [ src "http://barist.coffee.s3.amazonaws.com/ic_barista_logo.png", class "navigation__image"] []
                    ]
              , div [ class "navigation__text col-sm-4 col-md-4" ]
                    [ h1 []
                          [ text "Barista"]
                    ]
              ]
        , div [ class "navigation__href-session col-xs-6 col-sm-4 col-md-4" ]
              [ ul [ class "navigation__session-items"]
                    [ li [ class "navigation__session-item"] [ a [href "https://play.google.com/store/apps/details?id=makingdevs.com.mybarista"] [text "Descarga la applicación"] ]
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
                          [ img [ src (model.s3_asset |> Maybe.map .url_file |> Maybe.withDefault "http://barist.coffee.s3.amazonaws.com/avatar.png"), class "profile-page__avatar img-circle" ] []
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

grid : Model -> Html.Html Msg
grid model =
    div [ class "post-grid" ]
        [ div [ class "post-grid__items-container"]
              [ renderCheckins model ]
        ]

renderCheckins : Model -> Html.Html Msg
renderCheckins model =
  let
    items = List.map renderCheckin model.checkins
  in
    ul [ class "post-grid__items"] items

renderCheckin : Checkin -> Html.Html Msg
renderCheckin checkin =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/coffee.jpg"
    in
        li [ class "post-grid__item"
           , onClick (ShowCheckinDialog checkin)
           ]
           [ img [ class "post-grid__item-image"
                 , src <| (checkin.s3_asset
                          |> Maybe.map .url_file
                          |> Maybe.withDefault placeholder
                          )
                 ] []
           , Dialog.view
               ( Just ( checkinDialog checkin ) )
           ]


-- Footer
baristUrl : String
baristUrl =
    "http://www.barist.coffee/"
storeUrl : String
storeUrl =
    "https://play.google.com/store/apps/details?id=makingdevs.com.mybarista"
devsUrl : String
devsUrl =
    "http://makingdevs.com"

footer : Html.Html Msg
footer =
    div [ class "footer-main" ]
        [ div [ class "footer-nav" ]
              [ ul [ class "footer__nav-items"]
                    [ li [ class "footer__nav-item" ]
                         [ a [ href baristUrl ]
                             [ text "Barista" ]
                         ]
                    , li [ class "footer__nav-item" ]
                         [ a [ href storeUrl ]
                             [ text "Google Play" ]
                         ]
                    , li [ class "footer__nav-item col" ]
                         [ a [ href devsUrl ]
                             [ text "Making Devs"]
                         ]
                    ]
              ]
        ]


-- CHECK IN DIALOG
checkinDialog: Checkin -> Dialog.Config Msg
checkinDialog checkin =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/coffee.jpg"
    in
        { closeMessage = Just CancelCheckinDialog
        , header = Nothing
        , body = Just ( img [ class "img-responsive"
                            , src <| (checkin.s3_asset
                                     |> Maybe.map .url_file
                                     |> Maybe.withDefault placeholder
                                     )
                            ] []
                      )
        , footer =
            Nothing
        }

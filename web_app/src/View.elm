
module View exposing (..)

import Models exposing (Model, Checkin, CheckinComment)
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
    let
        brand = "http://barist.coffee.s3.amazonaws.com/ic_barista_logo.png"

        url =  "https://play.google.com/store/apps/details?id=makingdevs.com.mybarista"

    in
        div[ class "navigation-main" ]
            [ div [ class "navigation__brand" ]
                  [ div [ class "navigation__brand-container" ]
                        [ img [ src brand
                              , class "img-responsive" ] []
                        ]
                  , div [ class "navigation__text" ]
                      [ h1 []
                            [ text "Barista" ]
                      ]
                  ]
            , div [ class "navigation__href-session" ]
                  [ ul [ class "navigation__session-items" ]
                       [ li [ class "navigation__session-item" ]
                            [ a [ href url ]
                                [ text "Descarga la applicación" ]
                            ]
                       ]
                  ]
            ]


-- Profile
profile : Model -> Html.Html Msg
profile model =
    div [ class "profile-page" ]
        [ div [ class "profile-page__header"]
              [ div [ class "profile-page__author-container" ]
                    [ userAvatar model
                    , userInfo model
                    ]
              ]
        ]

userAvatar : Model -> Html Msg
userAvatar model =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/avatar.png"
    in
        div [ class "profile-page__avatar-container" ]
            [ img [ src (model.s3_asset
                          |> Maybe.map .url_file
                          |> Maybe.withDefault placeholder)
                    , class "profile-page__avatar img-circle" ]
                    []
            ]

userInfo : Model -> Html Msg
userInfo model =
    div [ class "profile-page__user-info" ]
        [ div [ class "profile-page__username" ]
              [ p []
                  [ text model.username ]
              ]
        , div [ class "profile-page__statistics" ]
              [ p []
                  [ text ((toString model.checkins_count) ++ " checkins") ]
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
    model.checkins
        |> List.map (renderCheckin model)
        |> ul [ class "post-grid__items" ]

renderCheckin : Model -> Checkin -> Html.Html Msg
renderCheckin model checkin =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/coffee.jpg"
    in
        li [ class "post-grid__item" ]
           [ img [ class "img-responsive"
                 , src <| (checkin.s3_asset
                          |> Maybe.map .url_file
                          |> Maybe.withDefault placeholder
                          )
                 , onClick (ShowCheckin checkin)
                 ] []
           , Dialog.view
               ( if checkin.show_checkin |> Maybe.withDefault False then
                     Just ( checkinDialog model checkin )
                 else
                     Nothing
               )
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
checkinDialog: Model -> Checkin -> Dialog.Config Msg
checkinDialog model checkin =
    let
        placeholder = "http://barist.coffee.s3.amazonaws.com/coffee.jpg"
    in
        { closeMessage = Just (HideCheckin checkin)
        , header = Nothing
        , body = Just ( div [ class "post-preview"]
                            [ div [ class "post-preview__image-container" ]
                                  [
                                   img [ class "img-responsive post-preview__image"
                                       , src <| (checkin.s3_asset
                                                |> Maybe.map .url_file
                                                |> Maybe.withDefault placeholder
                                                )
                                       ] []
                                  ]
                            , div [ class "post-preview__comments-container" ]
                                  [ div [ class "comments-container__header" ]
                                        [ div [ class "header__avatar-container" ]
                                              [ img [ class "header__avatar img-circle"
                                                    , src ( model.s3_asset
                                                          |> Maybe.map .url_file
                                                          |> Maybe.withDefault placeholder
                                                          )
                                                    ] []
                                              ]
                                        , div [ class "header__checkin-author" ]
                                              [ div [ class "checkin-author__user-container" ]
                                                    [ b []
                                                        [text checkin.author ]
                                                    ]
                                              , div []
                                                    {- TODO: Validate Venue -}
                                                    [  renderVenue checkin ]
                                              ]
                                        , div [ class "header__checkin-rating" ]
                                              [ span [ class "glyphicon glyphicon-star" ] []
                                              , p []
                                                  [ text (Maybe.withDefault "0" checkin.rating) ]]
                                        ]
                                  , div [ class "comments-container__body" ]
                                        [ div [ class "body__author-note" ]
                                              [ text (Maybe.withDefault "" checkin.note) ]
                                        , hr [] []
                                        , div [ class "body__other-comments" ]
                                              [ renderComments checkin ]
                                        ]
                                  ]
                            ]
                      )
        , footer =
            Nothing
        }

renderVenue : Checkin -> Html.Html Msg
renderVenue checkin =
    div [ class "checkin-author__venue-container" ]
        [ span [ class "glyphicon glyphicon-map-marker"] []
        , p []
            [ text ( checkin.venue
                   |> Maybe.map .name
                   |> Maybe.withDefault "Venue"
                   )
            ]
        ]


renderComments : Checkin -> Html.Html Msg
renderComments checkin =
    checkin.comments
        |> List.map renderComment
        |> ul [ class "comments__items" ]

renderComment : CheckinComment -> Html.Html Msg
renderComment comment =
    li []
       [ div [ class "comments__item-container"]
             [ p [ class "item-container__header" ]
                 [  b []
                      [ text (comment.user.username ++ ":") ]
                 ]
             , p [ class "item-container__body" ]
                 [ text (comment.body) ]
             ]
       ]

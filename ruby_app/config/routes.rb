Rails.application.routes.draw do

  resources :comments
  resources :checkins
  resources :users
  resources :circles
  resources :barista

  resources :users do
    resources :checkins
  end
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

   get '/login/user/', to: 'users#login', as: 'login'
   post 'checkins/:id/circleFlavor', to: 'circles#create', as: 'circleFlavor'
   post '/checkin/photo/save', to: 's3_asset#save_photo_by_checkin', as: 's3'
   get '/checkins/:id/comments', to: 'checkins#comments', as: 'checkinsComments'
   get '/s3asset/:s3_asset', to:'s3_asset#photo_url_s3', as: 'photo_s3'
   post '/checkins/:id/setRating', to: 'checkins#setRatingInCheckin', as: 'saveRating'
   post '/checkins/:id/setNote', to: 'checkins#setNoteInCheckin', as: 'saveNote'
   get '/search/users', to:'users#search', as: 'searchUsers'
   get '/foursquare/searh_venues', to: 'foursquare#searh_venues', as: 'api_foursquare_index'
   post '/barista/:id/save', to: 'barista#create', as: 'saveBarista'
   get '/foursquare/searh_venues_id', to: 'foursquare#search_venue_by_id', as: 'api_foursquare_search_id'
   #post '/barista/photo/:checkin_id', to: 's3_asset#photo_barista_s3', as: 'photoBarista'
   post '/barista/photo/save_photo', to: 's3_asset#save_photo_by_barista', as: 's3_barista'
   post '/users/photo/save', to: 's3_asset#save_photo_by_user', as: 's3_users'


end

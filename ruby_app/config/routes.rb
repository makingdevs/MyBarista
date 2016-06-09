Rails.application.routes.draw do

  resources :comments
  resources :checkins
  resources :users
  resources :circles
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

   get '/login/user/', to: 'users#login', as: 'login'
   post 'checkins/:id/circleFlavor', to: 'circles#create', as: 'circleFlavor'
   post '/users/image/profile', to: 'users#imageProfile', as: 's3'
   get '/checkins/:id/comments', to: 'checkins#comments', as: 'checkinsComments'
end

Rails.application.routes.draw do
  resources :comments
  resources :checkins
  resources :users
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

   get '/login/user/', to: 'users#login', as: 'login'

   post 'checkins/:id/circleFlavor', to: 'checkins#circleFlavor', as: 'circleFlavor'
end

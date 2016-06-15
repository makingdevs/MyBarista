class FoursquareController < ApplicationController
  def index
  	current_date = Time.now.strftime("%Y%m%d")
  	client = Foursquare2::Client.new(:api_version => current_date,
  		:client_id => '', 
  		:client_secret => '')
  	client.user(201716897)
  	render json: client
  end

  def show
  end

  def create
  end

  def update
  end
end

class FoursquareController < ApplicationController

  def initialize
    current_date = Time.now.strftime("%Y%m%d")
    @client = Foursquare2::Client.new(:api_version => current_date,
      :client_id => Rails.application.secrets.foursquare_id, 
      :client_secret => Rails.application.secrets.foursquare_secret)
  end
  
  def index
  end

  def show
  end

  def create
  end

  def update
  end

  def searh_venues
  	current_date = Time.now.strftime("%Y%m%d")
  	client = Foursquare2::Client.new(:api_version => current_date,
  		:client_id => Rails.application.secrets.foursquare_id, 
  		:client_secret => Rails.application.secrets.foursquare_secret)
  	venues_near = client.search_venues(:ll => "#{params[:latitude]},#{params[:longitude]}", :query => params[:query]) 
  	#p "#{venues_near.venues.first.id}"
    render :json => venues_near.venues
  end

  def search_venue_by_id
    venue_detail = @client.venue(params[:venue_id])
    if venue_detail != nil
      render json:venue_detail
    end
  end
end

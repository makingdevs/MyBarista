class CheckinsController < ApplicationController
  before_action :set_checkin, only: [:show, :update, :destroy]

  # GET /checkins
  def index
    @user = User.find_by username: params['username']
    @checkins = Checkin.where(:user => @user).all
    puts "GET"
    render json: @checkins
  end

  # GET /checkins/1
  def show
    puts "show"
    render json: @checkin
  end

  # POST /checkins
  def create
    save_venue(params['idVenueFoursquare'])
    #@checkin = Checkin.new(checkin_params)
    #user = User.find_by username: params['username']
    #@checkin.user = user
    #if @checkin.save
    #  render json: @checkin, status: :created, location: @checkin
    #else
    #  render json: @checkin.errors, status: :unprocessable_entity
    #end
  end

  # PATCH/PUT /checkins/1
  def update
    if @checkin.update(checkin_params)
      render json: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  def setRatingInCheckin
    puts params.inspect
    @checkin = Checkin.find(params[:id])
    @checkin.rating = params[:rating]
    if @checkin.save
      render json: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  def save_venue(venue_id)
    venue_location = venue_id != nil ? search_venue_by_id(venue_id) : ""
    puts "location: "+venue_location.to_s
  end

  def search_venue_by_id(venue_id)
    current_date = Time.now.strftime("%Y%m%d")
    @client = Foursquare2::Client.new(:api_version => current_date,
      :client_id => Rails.application.secrets.foursquare_id, 
      :client_secret => Rails.application.secrets.foursquare_secret)
    venue_detail = @client.venue(venue_id)
    if venue_detail != nil
      return venue_detail.location.formattedAddress
    end
  end

  # DELETE /checkins/1
  def destroy
    @checkin.destroy
  end

  #GET /checkins/id/comments
  def comments
    checkin = Checkin.find(params[:id])
    comments = Comment.where(:checkin => checkin).all
    render json:comments
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_checkin
      @checkin = Checkin.find(params[:id])
    end

    # Only allow a trusted parameter "white list" through.
    def checkin_params
      params.permit(:method, :origin, :price, :note, :rating)
    end
end

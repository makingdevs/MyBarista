class CheckinsController < ApplicationController
  before_action :set_checkin, only: [:show, :update, :destroy]

  # GET /checkins
  def index
    @user = User.find_by username: params['username']
    @checkins = Checkin.where(:user => @user).all
    puts "GET"
    render json: @checkins.reverse()
  end

  # GET /checkins/1
  def show
    puts "show"
    render json: @checkin
  end

  # POST /checkins
  def create
    @venue = search_venue(params['idVenueFoursquare'])
    if @venue == nil
      @venue = save_venue(params['idVenueFoursquare'])
    end
    @s3_asset = search_s3asset(params['idS3asset'])
    @checkin = Checkin.new(checkin_params)
    user = User.find_by username: params['username']
    @checkin.user = user
    @checkin.venue = @venue
    @checkin.s3_asset = @s3_asset
    if @checkin.save
      render json: @checkin, status: :created, location: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /checkins/1
  def update
    # TODO: Esto tiene que ir todo junto
    @venue = search_venue(params['idVenueFoursquare'])
    if @venue == nil
      @venue = save_venue(params['idVenueFoursquare'])
    end
    @s3_asset = search_s3asset(params['idS3asset'])
    @checkin.s3_asset = @s3_asset
    @checkin.venue = @venue
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

  def setNoteInCheckin
    puts params.inspect
    @checkin = Checkin.find(params[:id])
    @checkin.note = params[:note]
    if @checkin.save
      render json: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
  end
    
  end

  def search_venue(venue_id)
    if venue_id != nil
      @venue = Venue.find_by venue_id_foursquare: venue_id
      return @venue
    end
  end

  def save_venue(venue_id)
    venue_detail = venue_id != nil ? search_venue_foursquare_by_id(venue_id) : "venue_id_nil"
    if venue_detail != "venue_id_nil"
      @venue = Venue.create(venue_id_foursquare: venue_id, name:venue_detail.name,formatted_address: venue_detail.location.formattedAddress.join(''))
      if @venue
        return @venue
      end
    end
  end

  def search_venue_foursquare_by_id(venue_id)
    current_date = Time.now.strftime("%Y%m%d")
    @client = Foursquare2::Client.new(:api_version => current_date,
      :client_id => Rails.application.secrets.foursquare_app_id, 
      :client_secret => Rails.application.secrets.foursquare_app_secret,
      :ssl => { :verify =>false})
    venue_detail = @client.venue(venue_id)
    if venue_detail != nil
      return venue_detail
    end
  end

  def search_s3asset(s3_asset_id)
    @s3_asset = S3Asset.find_by id: s3_asset_id
    if  @s3_asset
      return @s3_asset
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
      params.permit(:method, :state, :origin, :price, :note, :rating, :created_at, :s3_asset)
    end
end

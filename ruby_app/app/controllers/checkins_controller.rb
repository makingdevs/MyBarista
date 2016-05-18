class CheckinsController < ApplicationController
  before_action :set_checkin, only: [:show, :update, :destroy]

  # GET /checkins
  def index
    @checkins = Checkin.all
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
    @checkin = Checkin.new(checkin_params)
    user = User.find_by username: params['username']
    @checkin.user = user
    if @checkin.save
      render json: @checkin, status: :created, location: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /checkins/1
  def update
    if @checkin.update(checkin_params)
      render json: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  # DELETE /checkins/1
  def destroy
    @checkin.destroy
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_checkin
      @checkin = Checkin.find(params[:id])
    end

    # Only allow a trusted parameter "white list" through.
    def checkin_params
      params.permit(:method, :origin, :price, :note)
    end
end

class BaristaController < ApplicationController
  before_action :set_barista, only: [:show]
  
  def create
    @checkin = Checkin.find(params['id'])
    @baristum = @checkin.baristum || (Baristum.new())
    @baristum.name = barista_params["name"]
    @s3_asset = S3Asset.find_by id: params["s3_asset"]
    @baristum.s3_asset = @s3_asset
    @baristum.save
    @checkin.baristum = @baristum
    if @checkin.save
      render :json => @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  def show
    render json: @baristum
  end

  rescue_from ActiveRecord::RecordNotFound, :with => :record_not_found

  private
    def set_barista
      @baristum = Baristum.find(params[:id])
    end

    def barista_params
      params.permit(:name)
    end

    def record_not_found
      render :json => "Not Found", :status => 404
    end

end

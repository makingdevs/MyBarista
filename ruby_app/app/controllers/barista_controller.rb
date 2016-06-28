class BaristaController < ApplicationController
  before_action :set_barista, only: [:show]

  def create
    @checkin = Checkin.find(params['id'])
    @s3_asset = S3Asset.find(params["s3_asset"])
    @baristum = Baristum.new(barista_params)
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

  private
    def set_barista
      @baristum = Baristum.find(params[:id])
    end

    def barista_params
      params.permit(:name)
    end


end

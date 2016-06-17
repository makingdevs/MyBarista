class BaristaController < ApplicationController
  before_action :set_barista, only: [:show]

  def create
    @checkin = Checkin.find(params['id'])
    @baristum = Baristum.new(barista_params)
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

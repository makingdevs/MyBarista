class BaristaController < ApplicationController
  before_action :set_barista, only: [:show]

  def create
    @checkin = Checkin.find(params['id'])
    puts @checkin
    @baristum = Baristum.new(barista_params)
    @checkin.baristum = @baristum
    if @checkin.save
      render json: @baristum, status: :created, location: @checkin
    else
      render json: @baristum.errors, status: :unprocessable_entity
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

class CirclesController < ApplicationController
  before_action :set_circle_flavor, only: [:show]

  def create
    @checkin =  Checkin.find_by id: params['id']
    @circle_flavor = CircleFlavor.new(circle_flavor_params)
    @circle_flavor.save
    @checkin.circle_flavor = @circle_flavor
    if @checkin.save
      render json: @checkin, status: :created, location: @checkin
    else
      render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  def show
    render json: @circle_flavor
  end

  private
    def set_circle_flavor
      @circle_flavor = CircleFlavor.find(params[:id])
    end

    def circle_flavor_params
      params.permit(:sweetness,:acidity,:flowery,:spicy,:salty,:berries,:chocolate,:candy,:body,:cleaning)
    end

end

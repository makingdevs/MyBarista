require 'securerandom'

class UsersController < ApplicationController
  before_action :set_user, only: [:show, :update, :destroy]

  # GET /users
  def index
    @users = User.all

    render json: @users
  end

  # GET /users/1
  def show
    render json: @user
  end

  # POST /users
  def create
    @user = User.new(user_params)
    @user.token = Digest::MD5.hexdigest(params[:username]+params[:password])
    if @user.save
      render json: @user, status: :created, location: @user
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /users/1
  def update
    if @user.update(user_params)
      render json: @user
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  # GET /users/login   => username, password =>success 200 o 201 y user token , error 401
  def login
    @user = User.find_by username:params['username']
    if @user.authenticate(params['password'])
      render json: @user
    else
      render :json => {:error => "Unauthorized"}.to_json, :status => 401
    end
  end

  # DELETE /users/1
  def destroy
    @user.destroy
  end

  def imageProfile
    Aws.use_bundled_cert!
    s3 = Aws::S3::Resource.new(region: 'us-east-1')
    file_to_upload = s3.bucket('mybarista.com').object("#{Time.now()}_#{params['file'].original_filename}")
    file_to_upload.upload_file(params['file'].tempfile, acl:'public-read')
    save_image_s3(file_to_upload.public_url,file_to_upload.key,params['user'],params['checkin'])
  end

  def save_image_s3(url_photo_s3,name_photo,user_id,checkin_id)
    @save_file_s3 = S3Asset.new(url_file:url_photo_s3,name_file:name_photo,checkin_id:checkin_id)
    if @save_file_s3.save
      render json: @save_file_s3, status: :created
    else
      render json: @save_file_s3.errors, status: :unprocessable_entity
    end
  end

  def photo_url_s3
    photo_checkin = S3Asset.where({checkin_id: params['checkin_id']}).last()
    if photo_checkin != nil
      puts "Encontro... #{photo_checkin.inspect()}" 
      @photo = photo_checkin
      render json: @photo
    else
      puts "No se encontro registro..."
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_user
      @user = User.find(params[:id])
    end

    # Only allow a trusted parameter "white list" through.
    def user_params
      params.permit(:username,:name,:lastName,:password)
    end
end

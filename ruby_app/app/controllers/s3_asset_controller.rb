class S3AssetController < ApplicationController

  def initialize
    @bucket_region = Rails.application.secrets.aws_bucket_region
    @bucket_name = Rails.application.secrets.aws_bucket_name
  end

	def upload_image_to_s3(params)
    Aws.use_bundled_cert!
    s3 = Aws::S3::Resource.new(region: @bucket_region)
    file_to_upload = s3.bucket(@bucket_name).object("#{Time.now()}_#{params['file'].original_filename}")
    file_to_upload.upload_file(params['file'].tempfile, acl:'public-read')
    file_to_upload
  end

  def save_image_s3_asset(url_photo_s3,name_photo)
    @save_file_s3 = S3Asset.new(url_file:url_photo_s3,name_file:name_photo)
    @save_file_s3.save
    @save_file_s3
  end

  def save_photo_by_checkin
    result_file = upload_image_to_s3(params)
    s3_asset_result = save_image_s3_asset(result_file.public_url, result_file.key)
    @checkin = Checkin.find(params[:checkin])
    @checkin.s3_asset = s3_asset_result
    if @checkin.save
      render json: s3_asset_result, status: :created
    else
       render json: @checkin.errors, status: :unprocessable_entity
    end
  end

  def save_photo_by_barista
    result_file = upload_image_to_s3(params)
    s3_asset_result = save_image_s3_asset(result_file.public_url, result_file.key)
    render json: s3_asset_result
  end

  def save_photo_by_user
    result_file = upload_image_to_s3(params)
    s3_asset_result = save_image_s3_asset(result_file.public_url, result_file.key)
    @user = User.find_by id: params["user"]
    @user.s3_asset = s3_asset_result
    if @user.save
      render json: s3_asset_result, status: :created
    else
       render json: @user.errors, status: :unprocessable_entity
    end
  end

  def photo_url_s3
    photo_checkin = S3Asset.find_by id: params["s3_asset"]
    render json: photo_checkin
  end
end

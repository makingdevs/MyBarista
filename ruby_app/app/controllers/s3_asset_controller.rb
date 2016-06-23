class S3AssetController < ApplicationController

  def initialize
    @bucket_region = Rails.application.secrets.aws_bucket_region
    @bucket_name = Rails.application.secrets.aws_bucket_name
  end

	def imageProfile
    Aws.use_bundled_cert!
    s3 = Aws::S3::Resource.new(region: @bucket_region)
    file_to_upload = s3.bucket(@bucket_name).object("#{Time.now()}_#{params['file'].original_filename}")
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
      @photo = photo_checkin
      render json: @photo
    end
  end
end

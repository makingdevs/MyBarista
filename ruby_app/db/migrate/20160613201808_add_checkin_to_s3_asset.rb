class AddCheckinToS3Asset < ActiveRecord::Migration[5.0]
  def change
    add_reference :s3_assets, :checkin, foreign_key: true
  end
end

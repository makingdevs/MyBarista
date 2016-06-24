class AddReferenceCheckinToS3 < ActiveRecord::Migration[5.0]
  def change
    add_reference :checkins, :s3_asset, foreign_key:true
  end
end

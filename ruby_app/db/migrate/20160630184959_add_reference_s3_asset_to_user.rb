class AddReferenceS3AssetToUser < ActiveRecord::Migration[5.0]
  def change
    add_reference :users, :s3_asset, foreign_key: true
  end
end

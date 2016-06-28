class AddReferenceS3AssetToBarista < ActiveRecord::Migration[5.0]
  def change
    add_reference :barista, :s3_asset, foreign_key: true
  end
end

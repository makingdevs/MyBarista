class RemoveReferenceUSerToS3Asset < ActiveRecord::Migration[5.0]
  def change
    remove_reference :s3_assets, :user, foreign_key: true
  end
end

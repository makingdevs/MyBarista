class RemoveReferenceS3toChecking < ActiveRecord::Migration[5.0]
  def change
    remove_reference :s3_assets, :checkin, foreign_key: true
   end
end

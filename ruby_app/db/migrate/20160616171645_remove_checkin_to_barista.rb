class RemoveCheckinToBarista < ActiveRecord::Migration[5.0]
  def change
    remove_reference(:barista, :checkin,  foreign_key: true)
  end
end

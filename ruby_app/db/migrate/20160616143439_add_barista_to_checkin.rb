class AddBaristaToCheckin < ActiveRecord::Migration[5.0]
  def change
    add_reference :barista, :checkin, foreign_key:true
  end
end

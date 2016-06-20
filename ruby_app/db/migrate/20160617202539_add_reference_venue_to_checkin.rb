class AddReferenceVenueToCheckin < ActiveRecord::Migration[5.0]
  def change
    add_reference :checkins, :venue, foreign_key: true
  end
end

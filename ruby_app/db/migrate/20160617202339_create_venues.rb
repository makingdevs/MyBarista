class CreateVenues < ActiveRecord::Migration[5.0]
  def change
    create_table :venues do |t|
      t.string :venue_id_foursquare
      t.string :name
      t.string :formatted_address

      t.timestamps
    end
  end
end

class CreateCheckins < ActiveRecord::Migration[5.0]
  def change
    create_table :checkins do |t|
      t.integer :method , :null => false
      t.string :origin
      t.decimal :price, precision: 8, scale: 2
      t.string :note

      t.timestamps
    end
  end
end

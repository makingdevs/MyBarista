class CreateBarista < ActiveRecord::Migration[5.0]
  def change
    create_table :barista do |t|
      t.string :name

      t.timestamps
    end
  end
end

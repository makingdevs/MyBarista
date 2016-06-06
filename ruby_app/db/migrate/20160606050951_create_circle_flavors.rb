class CreateCircleFlavors < ActiveRecord::Migration[5.0]
  def change
    create_table :circle_flavors do |t|
      t.integer :sweeness
      t.integer :acidity
      t.integer :flowery
      t.integer :spicy
      t.integer :salty
      t.integer :berries
      t.integer :chocolate
      t.integer :candy
      t.integer :body
      t.integer :cleaning

      t.timestamps
    end
  end
end

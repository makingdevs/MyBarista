class CreateCircleFlavors < ActiveRecord::Migration[5.0]
  def change
    create_table :circle_flavors do |t|
      t.int :sweeness
      t.int :acidity
      t.int :flowery
      t.int :spicy
      t.int :salty
      t.int :berries
      t.int :chocolate
      t.int :candy
      t.int :body
      t.int :cleaning

      t.timestamps
    end
  end
end

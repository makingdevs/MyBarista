class AddElementsToCheckin < ActiveRecord::Migration[5.0]
  def change
    add_column :checkins, :rating, :string
  end
end

class AddCircleFlavorToChecking < ActiveRecord::Migration[5.0]
  def change
    add_reference :checkins, :circle_flavor, foreign_key: true
  end
end

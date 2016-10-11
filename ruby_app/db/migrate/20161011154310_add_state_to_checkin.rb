class AddStateToCheckin < ActiveRecord::Migration[5.0]
  def change
    add_column :checkins, :state, :string
  end
end

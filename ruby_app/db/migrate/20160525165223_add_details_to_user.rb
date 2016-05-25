class AddDetailsToUser < ActiveRecord::Migration[5.0]
  def change
    add_column :users, :password, :string
    add_column :users, :name, :string
    add_column :users, :lastName, :string
  end
end

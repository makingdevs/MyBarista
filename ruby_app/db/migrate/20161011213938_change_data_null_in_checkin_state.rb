class ChangeDataNullInCheckinState < ActiveRecord::Migration[5.0]
  def change
    execute "UPDATE checkins SET state= 'Otro'"
  end
end

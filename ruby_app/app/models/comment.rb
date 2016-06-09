class Comment < ApplicationRecord
  belongs_to :user
  belongs_to :checkin

  def as_json(options={})
    super(:only => [:body,:created_at],
        :include => {
        :user => {:only => [:username]},
      }
    )
  end
end

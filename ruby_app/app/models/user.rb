class User < ApplicationRecord
  validates :username, uniqueness: true
  has_many :checkins
  has_many :comments
  has_secure_password


  def as_json(options={})
    super(
      :methods => [:checkins_count]
    )
  end

  def checkins_count
    checkins.count
  end

end

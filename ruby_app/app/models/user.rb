class User < ApplicationRecord
  validates :username, uniqueness: true
  has_many :checkins
  has_many :comments
  has_secure_password

  def self.search(search)
    users = []
    search.split(" ").each do |parameter|
      users += where("name LIKE '%#{parameter}%' OR lastName LIKE '%#{parameter}%'")
      .or(where("username LIKE ?", "%#{parameter}%"))
    end
    users.uniq
  end

  def as_json(options={})
    super(
      :methods => [:checkins_count,:visible_name]
    )
  end

  def visible_name
    name ? "#{name} #{lastName}" : username.split("@").first
  end

  def checkins_count
    checkins.count
  end

end

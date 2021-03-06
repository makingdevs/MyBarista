class User < ApplicationRecord
  validates :username, uniqueness: true, presence: true
  validates :email, uniqueness: true, presence: true
  has_many :checkins
  has_many :comments
  has_secure_password
  belongs_to :s3_asset, optional:true

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
      :include => {
        :s3_asset => {:only => [:url_file]},
        :checkins => {
          :only => [:id],
          :include => {
            :s3_asset => {:only => [:id, :url_file]},
            :comments => {
              :only => [:body, :created_at],
              :include => {
                :user => { :only => [:username] }
              }
            }
          },
          :methods => [:author]
        }
      },
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

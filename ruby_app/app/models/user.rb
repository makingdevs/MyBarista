class User < ApplicationRecord
  validates :username, uniqueness: true
  has_many :checkins
  has_many :comments
  has_secure_password
end

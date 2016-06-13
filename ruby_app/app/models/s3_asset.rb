class S3Asset < ApplicationRecord
  belongs_to :user
  belongs_to :checkin
end

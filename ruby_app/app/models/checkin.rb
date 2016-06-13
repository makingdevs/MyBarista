class Checkin < ApplicationRecord
  enum method: [:Expresso,:Americano,:Goteo,:Prensa,:Sifón,:Otro ]
  has_many :comments
  belongs_to :user
  belongs_to :circle_flavor, optional: true
  has_one :s3_asset
end

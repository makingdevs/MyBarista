class Checkin < ApplicationRecord
  enum method: [:Expresso,:Americano,:Goteo,:Prensa,:Sifón,:Otro ]
  belongs_to :user
end

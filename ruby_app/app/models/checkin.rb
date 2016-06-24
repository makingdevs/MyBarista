class Checkin < ApplicationRecord
  enum method: [:Expresso,:Americano,:Goteo,:Prensa,:Sifón,:Otro ]
  has_many :comments
  belongs_to :user
  belongs_to :circle_flavor, optional: true
  belongs_to :s3_asset, optional:true
  belongs_to :baristum, optional: true
  belongs_to :venue, optional: true

  def as_json(options={})
    super(
      :include => {
        :s3_asset => {:only => [:url_file]},
        :baristum => {:only => [:name]}
      },
      :methods => [:author]
    )
  end

  def author
    user.username
  end

end

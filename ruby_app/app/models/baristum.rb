class Baristum < ApplicationRecord
	belongs_to :s3_asset, optional:true
	def as_json(options={})
    super(
      :include => {
        :s3_asset => {:only => [:url_file]}
      }
    )
  end
end

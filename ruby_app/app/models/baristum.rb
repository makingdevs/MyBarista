class Baristum < ApplicationRecord
	belongs_to :s3_asset, optional:true
end

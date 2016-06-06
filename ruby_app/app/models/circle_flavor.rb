class CircleFlavor < ApplicationRecord
	after_initialize :init
  def init
		self.sweetness ||= 0
		self.acidity ||= 0
		self.flowery ||= 0
		self.spicy ||= 0
		self.salty ||= 0
		self.berries ||= 0
		self.chocolate ||= 0
		self.candy ||= 0
		self.body ||= 0
		self.cleaning ||= 0
	end
end

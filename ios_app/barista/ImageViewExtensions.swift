//
//  ImageViewExtensions.swift
//  barista
//
//  Created by Ariana Gothwski on 21/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Kingfisher

extension UIImageView {
    
    func loadURL(url: String) {
        self.kf.setImage(with: URL(string: url))
    }
    func loadUrlWithPlaceholder(url: String, placeholder: UIImage) {
        self.kf.setImage(with: URL(string: url), placeholder: placeholder)
    }
    
    func circleFrame() {
        self.layer.cornerRadius = self.frame.size.width / 2;
        self.clipsToBounds = true
        self.layer.borderWidth = 3
        self.layer.borderColor = UIColor.white.cgColor
    }
}

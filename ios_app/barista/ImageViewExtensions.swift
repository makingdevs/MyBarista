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
    
    func loadURL(url: String, placeholder: UIImage) {
        self.kf.setImage(with: URL(string: url), placeholder: placeholder)
    }
}

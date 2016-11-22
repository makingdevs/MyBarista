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
    
    func loadUrlWithBlur(url: String) {
        let blur = UIBlurEffect(style: UIBlurEffectStyle.light)
        let blurView = UIVisualEffectView(effect: blur)
        self.kf.setImage(with: URL(string: url))
        blurView.frame = self.bounds
        self.addSubview(blurView)
    }
    
    func loadUrlWithPlaceholder(url: String, placeholder: UIImage) {
        self.kf.setImage(with: URL(string: url), placeholder: placeholder)
    }
    
    func loadAvatar(url: String) {
        self.layer.cornerRadius = self.frame.size.width / 2;
        self.clipsToBounds = true
        self.kf.setImage(with: URL(string: url))
    }
    
    func loadAvatarWithBorder(url: String) {
        self.layer.cornerRadius = self.frame.size.width / 2;
        self.clipsToBounds = true
        self.layer.borderWidth = 3
        self.layer.borderColor = UIColor.white.cgColor
        self.kf.setImage(with: URL(string: url))
    }
}

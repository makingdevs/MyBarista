//
//  ViewExtensions.swift
//  barista
//
//  Created by Ariana Gothwski on 21/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import FDRatingView

extension UIView {
    
    func loadRating (rating: Float) {
        let ratingView = FDRatingView(frame: CGRect(x: 32, y: 32, width: 16, height: 16),
                                      style:.star,
                                      numberOfElements: 5,
                                      fillValue: rating,
                                      color: UIColor.brown,
                                      lineWidth:0.7,
                                      spacing:3.0)
        self.addSubview(ratingView)
    }
}

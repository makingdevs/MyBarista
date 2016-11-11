//
//  ViewExtensions.swift
//  barista
//
//  Created by Ariana Gothwski on 21/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Cosmos

extension CosmosView {
    
    func loadRating(rating: Float) {
        self.settings.fillMode = .half
        self.rating = Double(rating)
    }
}

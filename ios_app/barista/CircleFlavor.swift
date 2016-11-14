//
//  CircleFlavor.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class CircleFlavor {
    var sweetness: Int?
    var acidity: Int?
    var flowery: Int?
    var spicy: Int?
    var salty: Int?
    var berries: Int?
    var chocolate: Int?
    var candy: Int?
    var body: Int?
    var cleaning: Int?
    
    init(sweetness: Int?, acidity: Int?, flowery: Int?, spicy: Int?, salty: Int?, berries: Int?, chocolate: Int?, candy: Int?, body: Int?, cleaning: Int?) {
        self. sweetness = sweetness
        self.acidity = acidity
        self.flowery = flowery
        self.spicy = spicy
        self.salty = salty
        self.berries = berries
        self.chocolate = chocolate
        self.candy = candy
        self.body = body
        self.cleaning = cleaning
    }
}

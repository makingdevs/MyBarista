//
//  Venue.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class Venue {
    var id: Int
    var name: String
    var location: [String]
    
    init(id: Int, name: String, location: [String]) {
        self.id = id
        self.name = name
        self.location = location
    }
}

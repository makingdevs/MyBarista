//
//  Venue.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class Venue {
    var id: String
    var name: String
    var location: [String]
    
    init(id: String, name: String, location: [String]) {
        self.id = id
        self.name = name
        self.location = location
    }
}

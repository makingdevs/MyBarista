//
//  VenueCommand.swift
//  barista
//
//  Created by Ariana Santillán on 08/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class VenueCommand {
    var latitude: String
    var longitude: String
    var query: String
    
    init(latitude: String, longitude: String, query: String) {
        self.latitude = latitude
        self.longitude = longitude
        self.query = query
    }
}

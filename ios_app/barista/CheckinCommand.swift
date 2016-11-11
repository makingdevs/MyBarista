//
//  CheckinCommand.swift
//  barista
//
//  Created by Ariana Santillán on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class CheckinCommand {
    var id: Int?
    var username: String?
    var method: String?
    var state: String?
    var origin: String?
    var price: String?
    var note: String?
    var rating: Float?
    var idS3Asset: Int?
    var idVenueFoursquare: String?
    var created_at: Date?
    var errorMessage: String?
    
    init(username: String, method: String, state: String, origin: String, price: String, idS3Asset: Int?, idVenueFoursquare: String?, created_at: Date) {
        self.username = username
        self.method = method
        self.state = state
        self.origin = origin
        self.price = price
        self.idS3Asset = idS3Asset
        self.idVenueFoursquare = idVenueFoursquare
        self.created_at = created_at
    }
    
    init(id: Int, note: String) {
        self.id = id
        self.note = note
    }
    
    init(id: Int, rating: Float) {
        self.id = id
        self.rating = rating
    }
    
    func validateCommand() -> Bool {
        if (self.origin?.isEmpty)! {
            errorMessage = "Agrega el municipio de origen"
            return false
        } else if (self.price?.isEmpty)! {
            errorMessage = "Agrega el precio de tu bebida"
            return false
        } else if idVenueFoursquare == nil {
            errorMessage = "Agrega el lugar de preparación"
            return false
        }
        return  true
    }
}

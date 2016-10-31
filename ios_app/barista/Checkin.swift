//
//  Checkin.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class Checkin {
    var id:NSInteger?
    var author:String?
    var method:String?
    var note:String?
    var origin:String?
    var state:String?
    var price:Float?
    var rating:Float?
    var urlPhoto:String?
    var createdAt:Date?
    
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:Float, rating: Float, urlPhoto:String, createdAt:Date?){
        self.id = id
        self.author = author
        self.method = method
        self.note = note
        self.origin = origin
        self.state = state
        self.price = price
        self.rating = rating
        self.urlPhoto = urlPhoto
        self.createdAt = createdAt
    }
}

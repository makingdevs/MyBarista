//
//  Checkin.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class Checkin {
    var id:NSInteger
    var author:String?
    var method:String?
    var note:String?
    var origin:String?
    var state:String?
    var price:String?
    var rating:Float?
    var s3Asset: S3Asset?
    var venue: String?
    var createdAt:Date?
    
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:String, rating: Float, s3Asset: S3Asset, venue: String?, createdAt:Date?){
        self.id = id
        self.author = author
        self.method = method
        self.note = note
        self.origin = origin
        self.state = state
        self.price = price
        self.rating = rating
        self.s3Asset = s3Asset
        self.venue = venue
        self.createdAt = createdAt
    }
    
    /* s3asset exists in JSON response */
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:String, rating: Float, venue: String?, createdAt:Date?) {
        self.id = id
        self.author = author
        self.method = method
        self.note = note
        self.origin = origin
        self.state = state
        self.price = price
        self.rating = rating
        self.venue = venue
        self.createdAt = createdAt
    }
    
    init(id: Int, note: String) {
        self.id = id
        self.note = note
    }
    
    init(id: Int, rating: Float) {
        self.id = id
        self.rating = rating
    }
}

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
    var venueId: Int?
    var circleFlavor: Int?
    var createdAt:Date?
    
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:String, rating: Float, s3Asset: S3Asset, venue: String?, circleFlavor: Int, createdAt:Date?){
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
        self.circleFlavor = circleFlavor
        self.createdAt = createdAt
    }
    
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:String, rating: Float, s3Asset: S3Asset, venue: String?, venueId: Int, circleFlavor: Int, createdAt:Date?){
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
        self.venueId = venueId
        self.circleFlavor = circleFlavor
        self.createdAt = createdAt
    }
    
    /* s3asset exists in JSON response */
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:String, rating: Float, venue: String?, circleFlavor: Int, createdAt:Date?) {
        self.id = id
        self.author = author
        self.method = method
        self.note = note
        self.origin = origin
        self.state = state
        self.price = price
        self.rating = rating
        self.venue = venue
        self.circleFlavor = circleFlavor
        self.createdAt = createdAt
    }
    
    init(id:NSInteger, author:String, method:String, note:String, origin:String, state: String, price:String, rating: Float, venue: String?, venueId: Int, circleFlavor: Int, createdAt:Date?) {
        self.id = id
        self.author = author
        self.method = method
        self.note = note
        self.origin = origin
        self.state = state
        self.price = price
        self.rating = rating
        self.venue = venue
        self.venueId = venueId
        self.circleFlavor = circleFlavor
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
    
    init(id: Int, circleFlavor: Int) {
        self.id = id
        self.circleFlavor = circleFlavor
    }
}

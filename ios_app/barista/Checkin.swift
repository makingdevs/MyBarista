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
    var method:String?
    var note:String?
    var origin:String?
    var price:Float?
    var urlPhoto:String?
    var createdAt:NSDate?
    
    init(id:NSInteger, method:String, note:String, origin:String, price:Float, urlPhoto:String){
        self.id = id
        self.method = method
        self.note = note
        self.origin = origin
        self.price = price
        self.urlPhoto = urlPhoto
    }
}
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
    var createdAt:NSDate?
    
    init(id:NSInteger){
        self.id = id
    }
}
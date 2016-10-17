//
//  User.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class User{
    var id: Int
    var username: String
    var password: String
    
    init(id: Int, username: String, password: String){
        self.id = id
        self.username = username
        self.password = password
    }
}
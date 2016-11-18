//
//  UserProfile.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class UserProfile {
    var id: Int?
    var username: String?
    var name: String?
    var lastName: String?
    var checkinsCount: Int?
    var visibleName: String?
    var s3asset: S3Asset?
    
    init(id: Int, username: String, name: String, lastName: String, checkinsCount: Int, visibleName: String, s3asset: S3Asset) {
        self.id = id
        self.username = username
        self.name = name
        self.lastName = lastName
        self.checkinsCount = checkinsCount
        self.visibleName = visibleName
        self.s3asset = s3asset
    }
    
    init(id: Int, username: String, name: String, lastName: String, checkinsCount: Int, visibleName: String) {
        self.id = id
        self.username = username
        self.name = name
        self.lastName = lastName
        self.checkinsCount = checkinsCount
        self.visibleName = visibleName
    }
    
    /* Update */
    init(id: Int, name: String, lastName: String) {
        self.id = id
        self.name = name
        self.lastName = lastName
    }
}

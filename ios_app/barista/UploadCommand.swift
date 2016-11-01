//
//  UploadCommand.swift
//  barista
//
//  Created by Ariana Santillán on 01/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class UploadCommand {
    
    var userId: String
    var checkinId: String
    var path: String
    
    init(userId: String, checkinId: String, path: String) {
        self.userId = userId
        self.checkinId = checkinId
        self.path = path
    }
}

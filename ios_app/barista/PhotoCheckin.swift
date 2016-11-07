//
//  PhotoCheckin.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class PhotoCheckin {
    var id: Int
    var fileName: String
    var urlFile: String
    var createdAt: String
    var updatedAt: String
    
    init(id: Int, fileName: String, urlFile: String, createdAt: String, updatedAt: String) {
        self.id = id
        self.fileName = fileName
        self.urlFile = urlFile
        self .createdAt = createdAt
        self.updatedAt = updatedAt
    }
}

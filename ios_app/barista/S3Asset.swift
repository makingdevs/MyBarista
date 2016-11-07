//
//  S3Asset.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class S3Asset {
    var id: Int?
    var urlFile: String?
    
    init(id: Int, urlFile: String) {
        self.id = id
        self.urlFile = urlFile
    }
}

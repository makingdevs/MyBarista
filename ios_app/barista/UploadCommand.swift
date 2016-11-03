//
//  UploadCommand.swift
//  barista
//
//  Created by MakingDevs on 01/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import UIKit

class UploadCommand {
    
    var userId: String
    var checkinId: String
    var image: UIImage
    var imagePath: String
    var imageName: String
    var imageFileName: String
    var imageMimeType: String
    
    init(userId: String, checkinId: String, image: UIImage, imagePath: String, imageName: String, imageFileName: String, imageMimeType: String) {
        self.userId = userId
        self.checkinId = checkinId
        self.image = image
        self.imagePath = imagePath
        self.imageName = imageName
        self.imageFileName = imageFileName
        self.imageMimeType = imageMimeType
    }
}

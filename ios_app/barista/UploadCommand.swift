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
    var userId: Int?
    var image: UIImage?
    
    init(image: UIImage?) {
        self.image = image
    }
    
    init(userId: Int, image: UIImage) {
        self.userId = userId
        self.image = image
    }
    
    func validateCommand() -> Bool {
        return image != nil
    }
}

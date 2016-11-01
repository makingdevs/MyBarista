//
//  S3AssetManager.swift
//  barista
//
//  Created by Ariana Santillán on 01/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire

class S3AssetManager {
    
    static func upload(uploadCommand: UploadCommand, onPhotoSuccess: @escaping (_ photoCheckin: PhotoCheckin) -> (), onError: @escaping (_ error: String) -> () ) {
        
        let checkinPhotoURL: String = "http://mybarista.makingdevs.com/checkin/photo/save"
        let parameters = [
            "url_file": uploadCommand.path
        ]
    }
}

//
//  S3AssetManager.swift
//  barista
//
//  Created by MakingDevs on 01/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire

class S3AssetManager {
    
    static func upload(uploadCommand: UploadCommand, onPhotoSuccess: @escaping (_ photoCheckin: PhotoCheckin) -> (), onError: @escaping (_ error: String) -> () ) {
        
        let checkinPhotoURL: String = "http://mybarista.makingdevs.com/checkin/photo/save"
        
        Alamofire.upload(
            multipartFormData: { multipartFromData in
                multipartFromData.append(
                    UIImagePNGRepresentation(uploadCommand.image)!,
                    withName: uploadCommand.imageName,
                    fileName: uploadCommand.imageFileName,
                    mimeType: uploadCommand.imageMimeType
                )
            },
            to: checkinPhotoURL,
            encodingCompletion: { encodingResult in
                switch encodingResult {
                case .success(let upload, _, _):
                    upload.responseJSON {
                        response in
                        print(response)
                    }
                case .failure(let encodingError):
                    print(encodingError)
                }
        })
    }
}

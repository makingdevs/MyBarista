//
//  S3AssetManager.swift
//  barista
//
//  Created by MakingDevs on 01/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class S3AssetManager {
    
    static func uploadCheckinPhoto(uploadCommand: UploadCommand, onPhotoSuccess: @escaping (_ photoCheckin: PhotoCheckin) -> (), onPhotoError: @escaping (_ error: String) -> () ) {
        
        let assetURL = try! URLRequest(url: URL(string: "\(Constants.urlBase)/barista/photo/save_photo")!, method: .post, headers: nil)
        let image: Data = UIImageJPEGRepresentation(uploadCommand.image!, 1)!
        let imageName: String = "file"
        let imageFileName: String = "checkin.png"
        let imageMimeType: String = "image/png"
        
        Alamofire.upload(
            multipartFormData: { multipartFromData in
                multipartFromData.append(image,
                    withName: imageName,
                    fileName: imageFileName,
                    mimeType: imageMimeType
                )
            },
            with: assetURL,
            encodingCompletion: { encodingResult in
                switch encodingResult {
                case .success(let upload, _, _):
                    upload.responseJSON {
                        response in
                        if let value = response.result.value {
                            let json = JSON(value)
                            let s3Id = json["id"].intValue
                            let s3FileName = json["name_file"].stringValue
                            let s3UrlFile = json["url_file"].stringValue
                            let s3CreatedAt = json["created_at"].stringValue
                            let s3UpdatedAt = json["updated_at"].stringValue
                            let s3Asset = PhotoCheckin(id: s3Id, fileName: s3FileName, urlFile: s3UrlFile, createdAt: s3CreatedAt, updatedAt: s3UpdatedAt)
                                onPhotoSuccess(s3Asset)
                        }
                    }
                case .failure(let encodingError):
                    print(encodingError)
                }
        })
    }
    
    static func uploadUserPhoto(uploadCommand: UploadCommand,
                                onSuccess: @escaping (_ userPhoto: PhotoCheckin) -> (),
                                onError: @escaping (_ error: String) -> ()){
        let assetURL = try! URLRequest(url: URL(string: "\(Constants.urlBase)/users/photo/save")!, method: .post, headers: nil)
        let image: Data = UIImageJPEGRepresentation(uploadCommand.image!, 0.3)!
        let imageName: String = "file"
        let imageFileName: String = "photo_user.png"
        let imageMimeType: String = "image/png"
        
        let parameters = ["user": String(describing: uploadCommand.userId!)]
        
        Alamofire.upload(
            multipartFormData: { multipartFromData in
                for (key, value) in parameters {
                    multipartFromData.append(value.data(using: .utf8)!, withName: key)
                }
                multipartFromData.append(image,
                                         withName: imageName,
                                         fileName: imageFileName,
                                         mimeType: imageMimeType
                )
            },
            with: assetURL,
            encodingCompletion: { encodingResult in
                switch encodingResult {
                case .success(let upload, _, _):
                    upload.responseJSON {
                        response in
                        if let value = response.result.value {
                            let json = JSON(value)
                            let s3Id = json["id"].intValue
                            let s3FileName = json["name_file"].stringValue
                            let s3UrlFile = json["url_file"].stringValue
                            let s3CreatedAt = json["created_at"].stringValue
                            let s3UpdatedAt = json["updated_at"].stringValue
                            let s3Asset = PhotoCheckin(id: s3Id, fileName: s3FileName, urlFile: s3UrlFile, createdAt: s3CreatedAt, updatedAt: s3UpdatedAt)
                            onSuccess(s3Asset)
                        }
                    }
                case .failure(let encodingError):
                    print(encodingError)
                }
        })
    }
}

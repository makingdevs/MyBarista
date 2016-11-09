//
//  CheckinManager.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class CheckinManager {
    
    static func findAllCheckinsByUser(_ username: String, onSuccess:@escaping (_ checkins:[Checkin]) -> (), onError:@escaping (_ error:String) -> () ){
        let parameters = ["username":username]
        Alamofire.request("http://localhost:3000/checkins/", parameters: parameters).responseJSON{ response in
            switch response.result {
            case .success:
                var checkins = [Checkin]()
                if let value = response.result.value {
                    let json = JSON(value)
                    for(_, subJson) in json{
                        let checkinId = subJson["id"].intValue
                        let checkinAuthor = subJson["author"].stringValue
                        let checkinMethod = subJson["method"].stringValue
                        let checkinOrigin = subJson["origin"].stringValue
                        let checkinState = subJson["state"].stringValue
                        let checkinPrice = subJson["price"].stringValue
                        let checkinRating = subJson["rating"].floatValue
                        let checkinNote = subJson["note"].stringValue
                        let checkinS3Id = subJson["s3_asset"]["id"].intValue
                        let checkinS3Url = subJson["s3_asset"]["url_file"].stringValue
                        let urlPhoto = subJson["s3_asset"]["url_file"].stringValue
                        let checkinCreatedAt = subJson["created_at"].timeValue
                        let checkinS3 = S3Asset(id: checkinS3Id, urlFile: checkinS3Url)
                        let checkin = Checkin(id:checkinId, author: checkinAuthor, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, rating: checkinRating, s3Asset: checkinS3, urlPhoto: urlPhoto, createdAt: checkinCreatedAt as Date?)
                        checkins.append(checkin)
                    }
                }
                onSuccess(checkins)
            case .failure(let error):
                onError(error.localizedDescription)
            }
        }
    }
    
    static func create(checkinCommand: CheckinCommand, onSuccess: @escaping (_ checkin: Checkin) -> (), onError: @escaping (_ error: String) -> () ) {
        
        let createCheckinURL: String = "http://localhost:3000/checkins/"
        let parameters = ["username": checkinCommand.username!,
                          "method": checkinCommand.method!,
                          "state": checkinCommand.state!,
                          "origin": checkinCommand.origin!,
                          "price": checkinCommand.price!,
                          "idS3asset": checkinCommand.idS3Asset,
                          "idVenueFoursquare": checkinCommand.idVenueFoursquare,
                          "created_at": checkinCommand.created_at!] as [String : Any]
        
        Alamofire.request(createCheckinURL, method: .post, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    if let value = response.result.value {
                        let json = JSON(value)
                        let checkinId = json["id"].intValue
                        let checkinAuthor = json["author"].stringValue
                        let checkinMethod = json["method"].stringValue
                        let checkinState = json["state"].stringValue
                        let checkinOrigin = json["origin"].stringValue
                        let checkinPrice = json["price"].stringValue
                        let checkinRating = json["rating"].floatValue
                        let checkinNote = json["note"].stringValue
                        let checkinS3Id = json["s3_asset"]["id"].intValue
                        let checkinS3Url = json["s3_asset"]["url_file"].stringValue
                        let urlPhoto = json["s3_asset"]["url_file"].stringValue
                        let checkinCreatedAt = json["created_at"].timeValue
                        let checkinS3 = S3Asset(id: checkinS3Id, urlFile: checkinS3Url)
                        let checkin = Checkin(id: checkinId, author: checkinAuthor, method: checkinMethod, note: checkinNote, origin: checkinOrigin, state: checkinState, price: checkinPrice, rating: checkinRating, s3Asset: checkinS3, urlPhoto: urlPhoto ,createdAt : checkinCreatedAt as Date?)
                        onSuccess(checkin)
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
}

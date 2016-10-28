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
        Alamofire.request("http://mybarista.makingdevs.com/checkins/", parameters: parameters).responseJSON{ response in
            switch response.result {
            case .success:
                var checkins = [Checkin]()
                if let value = response.result.value {
                    let json = JSON(value)
                    for(_, subJson) in json{
                        let checkinId = subJson["id"].intValue
                        let checkinMethod = subJson["method"].stringValue
                        let checkinOrigin = subJson["origin"].stringValue
                        let checkinState = subJson["state"].stringValue
                        let checkinPrice = subJson["price"].floatValue
                        let checkinRating = subJson["rating"].floatValue
                        let checkinNote = subJson["note"].stringValue
                        let urlPhoto = subJson["s3_asset"]["url_file"].stringValue
                        let checkinCreatedAt = subJson["created_at"].timeValue
                        let checkin = Checkin(id:checkinId, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, rating: checkinRating, urlPhoto: urlPhoto, createdAt: checkinCreatedAt as Date?)
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
        
        let createCheckinURL: String = "http://mybarista.makingdevs.com/checkins/"
        let parameters = ["username": checkinCommand.username!,
                          "method": checkinCommand.method!,
                          "state": checkinCommand.state!,
                          "origin": checkinCommand.origin!,
                          "price": checkinCommand.price!,
                          "created_at": checkinCommand.created_at!] as [String : Any]
        
        Alamofire.request(createCheckinURL, method: .post, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    print("success")
                case .failure(_):
                    print("error")
                }
                
        }
    }
}

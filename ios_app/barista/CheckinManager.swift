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
    
    static func findAllCheckinsByUser(username: String, onSuccess:(checkins:[Checkin]) -> (), onError:(error:String) -> () ){
        let parameters = ["username":username]
        Alamofire.request(.GET, "http://mybarista.makingdevs.com/checkins", parameters: parameters).responseJSON{ response in
            switch response.result {
            case .Success:
                var checkins = [Checkin]()
                if let value = response.result.value {
                    let json = JSON(value)
                    for(_, subJson) in json{
                        let checkinId = subJson["id"].intValue
                        let checkinMethod = subJson["method"].stringValue
                        let checkinOrigin = subJson["origin"].stringValue
                        let checkinPrice = subJson["price"].floatValue
                        let checkinNote = subJson["note"].stringValue
                        let urlPhoto = subJson["s3_asset"]["url_file"].stringValue
                        let checkin = Checkin(id:checkinId, method:checkinMethod, note:checkinNote, origin: checkinOrigin, price:checkinPrice, urlPhoto: urlPhoto)
                        checkins.append(checkin)
                    }
                }
                onSuccess(checkins: checkins)
            case .Failure(let error):
                onError(error: error.description)
            }
        }
    }
}
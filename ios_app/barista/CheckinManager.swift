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
                        let checkinNote = subJson["note"].stringValue
                        let urlPhoto = subJson["s3_asset"]["url_file"].stringValue
                        let checkin = Checkin(id:checkinId, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, urlPhoto: urlPhoto)
                        checkins.append(checkin)
                    }
                }
                onSuccess(checkins)
            case .failure(let error):
                onError(error.localizedDescription)
            }
        }
    }
}

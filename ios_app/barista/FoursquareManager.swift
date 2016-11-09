//
//  FoursquareManager.swift
//  barista
//
//  Created by Ariana Santillán on 08/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class FoursquareManager {
    
    static func getVenuesNear(venueCommand: VenueCommand, onSuccess: @escaping (_ venues: [Venue]) -> (), onError: @escaping (_ error: String) -> () ) {
        let venuesURL: String = "http://localhost:3000/foursquare/searh_venues"
        let parameters = [ "latitude": venueCommand.latitude,
                           "longitude": venueCommand.longitude,
                           "query": venueCommand.query]
        
        Alamofire.request(venuesURL, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    var venues = [Venue]()
                    if let value = response.result.value {
                        let json = JSON(value)
                        for(_, json) in json {
                            let venueId = json["id"].intValue
                            let venueName = json["name"].stringValue
                            let venueFormatedAddress = json["location"]["formattedAddress"].arrayValue.map({$0.stringValue})
                            let venue = Venue(id: venueId, name: venueName, location: venueFormatedAddress)
                            venues.append(venue)
                        }
                    }
                    onSuccess(venues)
                case .failure(let error):
                    print(error.localizedDescription)
                }
        }
    }
}

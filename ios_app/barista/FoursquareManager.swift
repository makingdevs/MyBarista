//
//  FoursquareManager.swift
//  barista
//
//  Created by Ariana Santillán on 08/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire

class FoursquareManager {
    
    static func getVenuesNear(venueCommand: VenueCommand, onSuccess: @escaping (_ venue: Venue) -> (), onError: @escaping (_ error: String) -> () ) {
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
                    print(response)
                case .failure(let error):
                    print(error.localizedDescription)
                }
        }
    }
}

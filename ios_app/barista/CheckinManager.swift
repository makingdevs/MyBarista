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
        Alamofire.request("\(Constants.urlBase)/checkins/", parameters: parameters).responseJSON{ response in
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
                        let checkinVenue = subJson["venue"]["name"].stringValue
                        let checkinCreatedAt = subJson["created_at"].timeValue
                        if subJson["s3_asset"].exists() {
                            let s3assetId = subJson["s3_asset"]["id"].intValue
                            let s3assetUrl = subJson["s3_asset"]["url_file"].stringValue
                            let s3asset = S3Asset(id: s3assetId, urlFile: s3assetUrl)
                            let checkin = Checkin(id:checkinId, author: checkinAuthor, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, rating: checkinRating, s3Asset: s3asset, venue: checkinVenue, createdAt: checkinCreatedAt as Date?)
                            checkins.append(checkin)
                        } else {
                            let checkin = Checkin(id:checkinId, author: checkinAuthor, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, rating: checkinRating, venue: checkinVenue, createdAt: checkinCreatedAt as Date?)
                            checkins.append(checkin)
                        }
                    }
                }
                onSuccess(checkins)
            case .failure(let error):
                onError(error.localizedDescription)
            }
        }
    }
    
    static func create(checkinCommand: CheckinCommand, onSuccess: @escaping (_ checkin: Checkin) -> (), onError: @escaping (_ error: String) -> () ) {
        let createCheckinURL: String = "\(Constants.urlBase)/checkins/"
        let parameters = ["username": checkinCommand.username!,
                          "method": checkinCommand.method!,
                          "state": checkinCommand.state!,
                          "origin": checkinCommand.origin!,
                          "price": checkinCommand.price!,
                          "idS3asset": checkinCommand.idS3Asset ?? "",
                          "idVenueFoursquare": checkinCommand.idVenueFoursquare ?? "",
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
                        let checkinVenue = json["venue"]["name"].stringValue
                        let checkinCreatedAt = json["created_at"].timeValue
                        if json["s3_asset"].exists() {
                            let s3assetId = json["s3_asset"]["id"].intValue
                            let s3assetUrl = json["s3_asset"]["url_file"].stringValue
                            let s3asset = S3Asset(id: s3assetId, urlFile: s3assetUrl)
                            let checkin = Checkin(id:checkinId, author: checkinAuthor, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, rating: checkinRating, s3Asset: s3asset, venue: checkinVenue, createdAt: checkinCreatedAt as Date?)
                            onSuccess(checkin)
                        } else {
                            let checkin = Checkin(id: checkinId, author: checkinAuthor, method: checkinMethod, note: checkinNote, origin: checkinOrigin, state: checkinState, price: checkinPrice, rating: checkinRating, venue: checkinVenue, createdAt: checkinCreatedAt as Date?)
                            onSuccess(checkin)
                        }
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
    
    static func update(checkinId: Int, checkinCommand: CheckinCommand, onSuccess: @escaping (_ checkin: Checkin) -> (), onError: @escaping (_ error: String) -> () ) {
        let updateCheckinURL: String = "\(Constants.urlBase)/checkins/\(checkinId)/updateCheckin"
        let parameters = ["id": checkinId,
                          "username": checkinCommand.username!,
                          "method": checkinCommand.method!,
                          "state": checkinCommand.state!,
                          "origin": checkinCommand.origin!,
                          "price": checkinCommand.price!,
                          "idS3asset": checkinCommand.idS3Asset ?? "",
                          "idVenueFoursquare": checkinCommand.idVenueFoursquare ?? "",
                          "created_at": checkinCommand.created_at!] as [String : Any]
        
        Alamofire.request(updateCheckinURL, method: .post, parameters: parameters)
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
                        let checkinVenue = json["venue"]["name"].stringValue
                        let checkinCreatedAt = json["created_at"].timeValue
                        if json["s3_asset"].exists() {
                            let s3assetId = json["s3_asset"]["id"].intValue
                            let s3assetUrl = json["s3_asset"]["url_file"].stringValue
                            let s3asset = S3Asset(id: s3assetId, urlFile: s3assetUrl)
                            let checkin = Checkin(id:checkinId, author: checkinAuthor, method:checkinMethod, note:checkinNote, origin: checkinOrigin, state: checkinState, price:checkinPrice, rating: checkinRating, s3Asset: s3asset, venue: checkinVenue, createdAt: checkinCreatedAt as Date?)
                            onSuccess(checkin)
                        } else {
                            let checkin = Checkin(id: checkinId, author: checkinAuthor, method: checkinMethod, note: checkinNote, origin: checkinOrigin, state: checkinState, price: checkinPrice, rating: checkinRating, venue: checkinVenue, createdAt: checkinCreatedAt as Date?)
                            onSuccess(checkin)
                        }
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
    
    static func saveNote(checkinCommand: CheckinCommand, onSuccess: @escaping (_ checkin: Checkin) -> (), onError: @escaping (_ error: String) -> () ) {
        let updateNoteUrl: String = "\(Constants.urlBase)/checkins/\(checkinCommand.id!)/setNote"
        let parameters = ["id": checkinCommand.id ?? "",
                          "note": checkinCommand.note ?? ""] as [String : Any]
        
        Alamofire.request(updateNoteUrl, method: .post, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON() {
                response in
                switch response.result {
                case .success:
                    if let value = response.result.value {
                        let json = JSON(value)
                        let checkinId = json["id"].intValue
                        let checkinNote = json["note"].stringValue
                        let checkin = Checkin(id: checkinId, note: checkinNote)
                        onSuccess(checkin)
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
    
    static func saveRating(checkinCommand: CheckinCommand,
                           onSuccess: @escaping (_ checkin: Checkin) -> (),
                           onError: @escaping (_ error: String) -> ()) {
        let updateRatingUrl: String = "\(Constants.urlBase)/checkins/\(checkinCommand.id!)/setRating"
        let parameters = ["id": checkinCommand.id ?? "",
                          "rating": checkinCommand.rating ?? ""] as [String : Any]
        
        Alamofire.request(updateRatingUrl, method: .post, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON() {
                response in
                switch response.result {
                case .success:
                    if let value = response.result.value {
                        let json = JSON(value)
                        let checkinId = json["id"].intValue
                        let checkinRating = json["rating"].floatValue
                        let checkin = Checkin(id: checkinId, rating: checkinRating)
                        onSuccess(checkin)
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
    
    static func fetchCircleFlavor(checkinId: Int,
                                 onSuccess: @escaping (_ circleFlavour: CircleFlavor) -> (),
                                 onError: @escaping (_ error: String) -> ()) {
        
        let circleFlavorUrl: String = "\(Constants.urlBase)/circles/\(checkinId)"
        let parameters = ["id": checkinId]
        
        Alamofire.request(circleFlavorUrl, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    print(response)
                case .failure:
                    print(response)
                }
        }
    }
}

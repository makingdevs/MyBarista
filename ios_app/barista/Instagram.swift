//
//  Instagram.swift
//  barista
//
//  Created by marco antonio reyes  on 24/04/18.
//  Copyright © 2018 MakingDevs. All rights reserved.
//

//
//  Instagram.swift
//  PhotoBrowser
//
//  Created by Zhouqi Mo on 12/22/14.
//  Copyright (c) 2014 Zhouqi Mo. All rights reserved.
//

import Alamofire
import UIKit

enum Router: URLRequestConvertible {
    
    static let baseURLString = "https://api.instagram.com"
    static let clientID = "617d56a5472843419cf06bd61c104cf9"
    static let redirectURI = "http://users.barist.coffee/redirect"
    static let clientSecret = "c1ac8ba35af8432d815625cc8587eb13"
    
    case popularPhotos
    case requestOauthCode
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: [String: AnyObject]?) = {
            switch self {
            case .popularPhotos:
                let userPreferences = UserDefaults.standard
                let userID = userPreferences.value(forKey: "user_id") as! String
                let accessToken = userPreferences.value(forKey: "instagram_token")
                let params = ["access_token": accessToken]
                let pathString = "/v1/users/" + userID + "/media/recent"
                return (pathString, params as [String : AnyObject])
                
            case .requestOauthCode:
                let pathString = "/oauth/authorize/?client_id=" + Router.clientID + "&redirect_uri=" + Router.redirectURI + "&response_type=code"
                return (pathString, nil)
            }
        }()
        
        
        let baseURL = URL(string: Router.baseURLString)!
        let urlRequest = URLRequest(url: URL(string: result.path ,relativeTo:baseURL)!)
        let encoding = URLEncoding.default
        return try encoding.encode(urlRequest, with: result.parameters)
    }
    
    static func requestAccessTokenURLStringAndParms(code: String) -> (urlString: String, params: [String: AnyObject]) {
        let params = ["client_id": Router.clientID, "client_secret": Router.clientSecret, "grant_type": "authorization_code", "redirect_uri": Router.redirectURI, "code": code]
        let pathString = "/oauth/access_token"
        let urlString = Router.baseURLString + pathString
        return (urlString, params as [String : AnyObject])
    }
    
}


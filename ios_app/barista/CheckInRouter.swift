//
//  CheckInRouter.swift
//  barista
//
//  Created by MakingDevs on 25/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire

enum CheckInRouter: URLRequestConvertible {
    
    static let baseURLString = "http://localhost:3000/"
    
    case fetchList(String)
    
    func asURLRequest() throws -> URLRequest {
        var method: HTTPMethod {
            switch self {
            case .fetchList:
                return .get
            }
        }
        
        let url: URL = {
            let relativePath: String
            switch self {
            case .fetchList:
                relativePath = "checkins/"
            }
            var url = URL(string: CheckInRouter.baseURLString)!
            url.appendPathComponent(relativePath)
            return url
        }()
    
        let params: ([String: Any]?) = {
            switch self {
            case .fetchList(let username):
                return (["username": username])
            }
        }()
    
        var urlRequest = URLRequest(url: url)
        urlRequest.httpMethod = method.rawValue
        
        let encoding = JSONEncoding.default
        return try encoding.encode(urlRequest, with: params)
    }
}

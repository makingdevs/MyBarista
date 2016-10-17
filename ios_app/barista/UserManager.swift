//
//  UserManager.swift
//  barista
//
//  Created by Ariana Santillán on 14/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class UserManager {
    
    static func signin(loginCommand: LoginCommand, onSuccess:(user: User) -> (), onError:(error: String) -> () ) {
        let loginURL: String = "http://mybarista.makingdevs.com/login/user/"
        let parameters:[String:AnyObject]? = ["username": loginCommand.username!, "password": loginCommand.password!]
        Alamofire.request(.GET, loginURL, parameters: parameters).responseJSON {
            response in
            switch response.result {
            case .Success:
                print("Success")
            case .Failure(_):
                print(response.result)
            }
        }
    }
}
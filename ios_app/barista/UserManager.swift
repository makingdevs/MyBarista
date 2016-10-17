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
        
        let signinURL: String = "http://mybarista.makingdevs.com/login/user"
        let parameters:[String:AnyObject]? = ["username": loginCommand.username!,
                                              "password": loginCommand.password!]
        
        Alamofire.request(.GET, signinURL, parameters: parameters)
                 .validate(statusCode: 200..<201)
                 .responseJSON {
            response in
            switch response.result {
            case .Success:
                if let value = response.result.value {
                    let json = JSON(value)
                    let userID = json["id"].intValue
                    let userName = json["username"].stringValue
                    let userPass = json["password_digest"].stringValue
                    let user = User(id: userID, username: userName, password: userPass)
                    onSuccess(user: user)
                }
            case .Failure(_):
                let errorMessage : String
                // TODO: Switch statusCode and manage the other ones
                if response.response?.statusCode == 401 {
                    errorMessage = "Usuario o contraseña incorrectos"
                    onError(error: errorMessage)
                }
            }
        }
    }
}
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
    
    static func signin(loginCommand: LoginCommand, onSuccess:@escaping (_ user: User) -> (), onError:@escaping (_ error: String) -> () ) {
        
        let signinURL: String = "http://mybarista.makingdevs.com/login/user/"
        let parameters = ["username": loginCommand.username!,
                          "password": loginCommand.password!]
        
        Alamofire.request(signinURL, parameters: parameters)
                 .validate(statusCode: 200..<201)
                 .responseJSON {
            response in
            switch response.result {
            case .success:
                if let value = response.result.value {
                    let json = JSON(value)
                    let userID = json["id"].intValue
                    let userName = json["username"].stringValue
                    let userPass = json["password_digest"].stringValue
                    let user = User(id: userID, username: userName, password: userPass)
                    onSuccess(user)
                    print(response)
                }
            case .failure(_):
                let errorMessage : String
                if let statusCode = response.response?.statusCode {
                    switch(statusCode){
                    case 500,401:
                        errorMessage = "Usuario o contraseña incorrectos"
                    case _:
                        errorMessage = "Desconocido"
                    }
                    print(statusCode)
                    onError(errorMessage)
                }
            }
        }
    }
}

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
        
        let signinURL: String = "\(Constants.urlBase)/login/user/"
        let parameters = ["username": loginCommand.username!,
                          "password": loginCommand.password!]
        
        Alamofire.request(signinURL, parameters: parameters)
                 .validate(statusCode: 200..<202)
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
                    onError(errorMessage)
                }
            }
        }
    }
    
    static func signup(registrationCommand: RegistrtionCommand, onSuccess:@escaping (_ user: User) -> (), onError:@escaping (_ error: String) -> () ) {
        
        let signupURL: String = "\(Constants.urlBase)/users/"
        let parameters = ["email": registrationCommand.email!,
                          "username": registrationCommand.username!,
                          "password": registrationCommand.password!]
        
        Alamofire.request(signupURL, method: .post, parameters: parameters)
            .validate(statusCode: 200..<202)
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
                    }
                case .failure(_):
                    let errorMessage: String
                    if let statusCode = response.response?.statusCode {
                        switch(statusCode) {
                        case 422:
                            errorMessage = "El usuario ya se encuentra registrado"
                        case _:
                            errorMessage = "Desconocido"
                        }
                        onError(errorMessage)
                    }
                }
        }
    }
    
    static func fetchProfile(userId: Int,
                             onSuccess: @escaping (_ userProfile: UserProfile) -> (),
                             onError: @escaping (_ error: String) -> ()) {
        
        let userProfileUrl: String = "\(Constants.urlBase)/users/\(userId)"
        let parameters = ["id": userId]
        
        Alamofire.request(userProfileUrl, parameters: parameters)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    print(response)
                case .failure(let error):
                    print(error)
                }
        }
    }
}

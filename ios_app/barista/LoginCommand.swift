//
//  LoginCommand.swift
//  barista
//
//  Created by Ariana Santillán on 14/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class LoginCommand {
    
    var username: String?
    var password: String?
    var email: String?
    var token: String?
    var firstName: String?
    var lastName: String?
    var errorMessage: String?
    
    init(username: String, password: String){
        self.username = username
        self.password = password
    }
    
    init(username: String, password: String, email: String, token: String, firstName: String, lastName: String) {
        self.username = username
        self.password = password
        self.email = email
        self.token = token
        self.firstName = firstName
        self.lastName = lastName
    }
    
    func validateCommand() -> Bool {
        if (self.username?.isEmpty) != nil {
            self.errorMessage = "Nombre de usuario requerido"
            return false
        }else if (self.password?.isEmpty) != nil {
            self.errorMessage = "Contraseña requerida"
            return false
        }
        return true
    }
}

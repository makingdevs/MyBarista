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
    var errorMessage: String?
    
    init(username: String, password: String){
        self.username = username
        self.password = password
    }
    
    //Maybe method
    func validateCommand() -> Bool {
        if (self.username?.isEmpty)! {
            self.errorMessage = "Nombre de usuario requerido"
            return false
        } else if (self.password?.isEmpty)! {
            self.errorMessage = "Contraseña requerida"
            return false
        }
        return true
    }
}

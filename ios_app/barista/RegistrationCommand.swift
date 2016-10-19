//
//  RegistrationCommand.swift
//  barista
//
//  Created by Ariana Gothwski on 18/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class RegistrtionCommand {
    
    var username: String?
    var password: String?
    var confirmPassword: String?
    var email: String?
    var errorMessage: String?
    
    init(username: String, password: String, confirmPassword: String, email: String) {
        self.username = username
        self.password = password
        self.confirmPassword = confirmPassword
        self.email = email
    }
    
    func validateCommand() -> Bool {
        let regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,4}"
        let email = NSPredicate(format: "SELF MATCHES %@", regex)
        if !email.evaluate(with: self.email) {
            self.errorMessage = "Email inválido"
            return false
        } else if (self.username?.isEmpty)! {
            self.errorMessage = "Nombre de usuario requerido"
            return false
        } else if (self.password?.isEmpty)! {
            self.errorMessage = "Contraseña requerida"
            return false
        } else if self.password == self.email {
            self.errorMessage = "Por seguridad tu contraseña debe ser distinta de tu email"
            return false
        } else if self.password != self.confirmPassword {
            self.errorMessage = "Las contraseñas no coinciden"
            return false
        }
        return true
    }
}


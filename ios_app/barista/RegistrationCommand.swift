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
    
    init(username: String, password: String, confirmPassword: String, email: String) {
        self.username = username
        self.password = password
        self.confirmPassword = confirmPassword
        self.email = email
    }
    
    func validateCommand() -> Bool {
        let regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,4}"
        let email = NSPredicate(format: "SELF MATCHES %@", regex)
        // TODO: To improve validation
        return email.evaluate(with: self.email) &&
            !(self.username?.isEmpty)! &&
            !(self.password?.isEmpty)! &&
            self.password != self.email &&
            self.password == self.confirmPassword
    }
}


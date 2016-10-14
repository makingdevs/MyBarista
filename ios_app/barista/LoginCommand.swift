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
    
    init(username: String, password: String){
        self.username = username
        self.password = password
    }
    
    //Maybe method
    func validateCommand() -> Bool {
        let regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,4}"
        let username = NSPredicate(format:"SELF MATCHES %@", regex)
        return username.evaluateWithObject(self.username)
    }
}

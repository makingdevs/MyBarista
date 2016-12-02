//
//  FacebookProfile.swift
//  barista
//
//  Created by MakingDevs on 30/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class FacebookProfile {
    
    var id: Int?
    var firstName: String?
    var lastName: String?
    var email: String?
    var birthday: String?
    
    init(id: Int, firstName: String, lastName: String, email: String, birthday: String) {
        self.id = id
        self.firstName = firstName
        self.lastName = lastName
        self.email = email
        self.birthday = birthday
    }
}

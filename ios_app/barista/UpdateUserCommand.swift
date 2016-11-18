//
//  UpdateUserCommand.swift
//  barista
//
//  Created by MakingDevs on 18/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
class UpdateUserCommand {
    var id: Int?
    var name: String?
    var lastName: String?
    
    init(id: Int, name: String, lastName: String) {
        self.id = id
        self.name = name
        self.lastName = lastName
    }
    
    func validateCommand() -> Bool {
        return name != nil && lastName != nil
    }
}

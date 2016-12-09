//
//  CommentCommand.swift
//  barista
//
//  Created by MakingDevs on 09/12/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class CommentCommand {

    var author: String?
    var body: String?
    var checkinId: String?
    var createdAt: Date?
    
    init(author: String, body: String, checkinId: String, createdAt: Date) {
        self.author = author
        self.body = body
        self.checkinId = checkinId
        self.createdAt = createdAt
    }
}

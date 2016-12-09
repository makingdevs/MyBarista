//
//  Comment.swift
//  barista
//
//  Created by MakingDevs on 8/26/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation

class Comment {
    
    var author: String?
    var body: String?
    var created_at: Date?
    
    init(author: String, body: String, created_at: Date) {
        self.author = author
        self.body = body
        self.created_at = created_at
    }
}

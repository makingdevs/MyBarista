//
//  JSONExtensions.swift
//  barista
//
//  Created by Ariana Santillán on 21/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import SwiftyJSON

extension JSON {
    
    public var dateValue: NSDate? {
        get {
            switch self.type {
            case .string:
                return Formatter.jsonDateFormatter.date(from: self.object as! String) as NSDate?

            default:
                return nil
            }
        }
    }
    
    public var timeValue: NSDate? {
        get {
            switch self.type {
            case .string:
                return Formatter.jsonDateTimeFormatter.date(from: self.object as! String) as NSDate?
            default:
                return nil
            }
        }
    }
}

//
//  SignUpViewController.swift
//  barista
//
//  Created by MakingDevs on 9/2/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class SignUpViewController: UIViewController {
    
    @IBAction func sendRequest(sender: UIButton) {
        // let parameterzs  = [:]
        // Alamofire.request(.POST, "http://mybarista.makingdevs.com/user/neodevelop", parameters: parameters)
        Alamofire.request(.GET, "http://mybarista.makingdevs.com/user/neodevelop")
            .responseJSON { response in
            if let value = response.result.value{
                let json = JSON(value)
                print(json)
                let checkins = json["checkins"]
                for(_, subJson) in checkins {
                    print("***")
                    let id = subJson["id"].stringValue
                    print(id)
                    let author = subJson["author"].stringValue
                    print(author)
                }
            }
        }
    }
}

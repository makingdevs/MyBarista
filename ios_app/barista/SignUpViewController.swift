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
    
    
    @IBOutlet weak var mailUser: UITextField!
    @IBOutlet weak var userName: UITextField!
    @IBOutlet weak var passw: UITextField!
    @IBOutlet weak var cpassw: UITextField!
    
    
    func isValidEmail(_ testStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    func alertTest(_ testStr:String) -> Void {
        let alert = UIAlertController(title: "Alert", message: testStr, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "Click", style: UIAlertActionStyle.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    
    @IBAction func sendRequest(_ sender: UIButton) {
        let mail : String = mailUser.text!
        let user : String = userName.text!
        let pass : String = passw.text!
        let cpass : String = cpassw.text!
       
        let email:Bool = isValidEmail(mail)
        if email{
            if pass == cpass{
                alertTest("Registro exitoso")
                print("registro exitoso" + user)
            }else{
                alertTest("las contraseñas no coinciden")
                cpassw.text = ""
                passw.text = ""
            }
        }else{
            alertTest("email no valido")
            mailUser.text=""
            
        }
       
        // let parameterzs  = [:]
        // Alamofire.request(.POST, "http://mybarista.makingdevs.com/user/neodevelop", parameters: parameters)
        Alamofire.request("http://mybarista.makingdevs.com/user/neodevelop/")
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

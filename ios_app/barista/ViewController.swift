//
//  ViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    var user: User!
    var loginCommand: LoginCommand!
  
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    @IBAction func fetchUserData(_ sender: UIButton) {
        let username: String = usernameField.text!
        let password: String = passwordField.text!
        loginCommand = LoginCommand(username: username, password: password)
        
        if !username.isEmpty && !password.isEmpty {
            UserManager.signin(loginCommand: loginCommand,
                               onSuccess: { (user: User) -> () in
                                //TODO: To call shouldPerformSegue... method
                },
                               onError:{ (error: String) -> () in
                                //TODO: To call shouldPerformSegue... method
            })
        } else {
            print("Campos obligatorios")
        }
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return true
    }
}

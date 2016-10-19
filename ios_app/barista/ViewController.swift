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
    var performSignIn: Bool = false
  
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    @IBAction func fetchUserData(_ sender: UIButton) {
        let username: String = usernameField.text!
        let password: String = passwordField.text!
        loginCommand = LoginCommand(username: username, password: password)
        
        if !username.isEmpty && !password.isEmpty {
            UserManager.signin(loginCommand: loginCommand,
                               onSuccess: { (user: User) -> () in
                                self.user = user
                                // TODO: Ask if it is a correct way to perform a Segue
                                self.performSignIn = true
                                self.performSegue(withIdentifier: "PerformSignIn", sender: self)
                },
                               onError:{ (error: String) -> () in
                                // TODO: Show an error alert
            })
        } else {
            print("Campos obligatorios")
        }
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return performSignIn
    }
}

//
//  SignUpViewController.swift
//  barista
//
//  Created by MakingDevs on 9/2/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {
    
    var registrationCommand: RegistrtionCommand!
    
    @IBOutlet weak var emailField: UITextField!
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    @IBOutlet weak var confirmField: UITextField!
    
    @IBAction func sendRequest(_ sender: UIButton) {
        let email: String = emailField.text!
        let username: String = usernameField.text!
        let password: String = passwordField.text!
        let confirm: String = confirmField.text!
        
        registrationCommand = RegistrtionCommand(username: username, password: password, confirmPassword: confirm, email: email)
        
        if registrationCommand.validateCommand() {
            print("Your form is great for Sign Up")
        } else {
            print("Something is wrong with your data")
        }
    }
}

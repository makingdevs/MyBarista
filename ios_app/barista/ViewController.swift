//
//  ViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    var loginCommand: LoginCommand!
  
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject?) -> Bool {
        loginCommand = LoginCommand.init(username: usernameField.text!, password: passwordField.text!)
        
        return !usernameField.text!.isEmpty && !passwordField.text!.isEmpty
    }
}


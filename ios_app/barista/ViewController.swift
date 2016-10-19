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
        
        if loginCommand.validateCommand() {
            UserManager.signin(loginCommand: loginCommand,
                               onSuccess: { (user: User) -> () in
                                self.user = user
                                // TODO: Ask if it is a correct way to perform a Segue
                                self.performSignIn = true
                                self.performSegue(withIdentifier: "PerformSignIn", sender: self)
                },
                               onError:{ (error: String) -> () in
                                self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        } else {
            self.present(self.showErrorAlert(message: loginCommand.errorMessage!), animated: true)
        }
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Ocurrió un error", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return identifier == "PerformSignIn" ? performSignIn : true
    }
}

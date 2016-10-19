//
//  SignUpViewController.swift
//  barista
//
//  Created by MakingDevs on 9/2/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {
    
    var user: User!
    var registrationCommand: RegistrtionCommand!
    var perfomrSignUp: Bool = false
    
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
            UserManager.signup(registrationCommand: registrationCommand,
                               onSuccess: { (user: User) -> () in
                                self.user = user
                                self.perfomrSignUp = true
                                self.performSegue(withIdentifier: "PerformSignUp", sender: self)
                },
                               onError: { (error: String) -> () in
                                self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        } else {
            self.present(self.showErrorAlert(message: registrationCommand.errorMessage!), animated: true)
        }
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Ocurrió un error", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "PerformSignUp" {
            let navigationController: UINavigationController = segue.destination as! UINavigationController
            let checkinsTableController = navigationController.topViewController as! CheckinsTableViewController
            checkinsTableController.user = user
        }
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return perfomrSignUp
    }
}

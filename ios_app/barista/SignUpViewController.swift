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
    @IBOutlet weak var signUpButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        emailField.underlined()
        emailField.attributedPlaceholder = NSAttributedString(string: "Correo",
                                                                 attributes: [NSAttributedStringKey.foregroundColor: UIColor.lightGray])
        usernameField.underlined()
        usernameField.attributedPlaceholder = NSAttributedString(string: "Usuario",
                                                              attributes: [NSAttributedStringKey.foregroundColor: UIColor.lightGray])
        passwordField.underlined()
        passwordField.attributedPlaceholder = NSAttributedString(string: "Contraseña",
                                                                 attributes: [NSAttributedStringKey.foregroundColor: UIColor.lightGray])
        confirmField.underlined()
        confirmField.attributedPlaceholder = NSAttributedString(string: "Confirma Contraseña",
                                                                 attributes: [NSAttributedStringKey.foregroundColor: UIColor.lightGray])
        signUpButton.layer.cornerRadius = 10
        signUpButton.clipsToBounds = true
        
        self.hideKeyboardWhenTappedAround()
    }
    
    @IBAction func sendRequest(_ sender: UIButton) {
        let email: String = emailField.text!
        let username: String = usernameField.text!
        let password: String = passwordField.text!
        let confirm: String = confirmField.text!
        
        registrationCommand = RegistrtionCommand(username: username, password: password, confirmPassword: confirm, email: email)
        
        if registrationCommand.validateCommand() {
            UserManager.signup(registrationCommand: registrationCommand,
                               onSuccess: { (user: User) -> () in
                                self.setUserPreferences(currentUser: user)
                                _ = self.navigationController?.popViewController(animated: true)
                },
                               onError: { (error: String) -> () in
                                self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        } else {
            if let errorMessage = registrationCommand.errorMessage {
                self.present(self.showErrorAlert(message: errorMessage), animated: true)
            }else{
                self.present(self.showErrorAlert(message: "Error desconocido"), animated: true)
            }
        }
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Ocurrió un error", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
    }
    
    func setUserPreferences(currentUser: User) {
        let userPreferences = UserDefaults.standard
        userPreferences.set(currentUser.username, forKey: "currentUser")
        userPreferences.set(currentUser.id, forKey: "currentUserId")
        userPreferences.set(currentUser.password, forKey: "currentUserPassword")
        userPreferences.synchronize()
    }
}

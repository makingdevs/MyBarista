//
//  ViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import FBSDKLoginKit

class ViewController: UIViewController, FBSDKLoginButtonDelegate {
    
    var loginCommand: LoginCommand!
    var performSignIn: Bool = false
  
    @IBOutlet weak var facebookLoginButton: FBSDKLoginButton!
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        facebookLoginButton.delegate = self
        facebookLoginButton.readPermissions = ["email", "public_profile"]
    }
    
    @IBAction func fetchUserData(_ sender: UIButton) {
        let username: String = usernameField.text!
        let password: String = passwordField.text!
        loginCommand = LoginCommand(username: username, password: password)
        
        if loginCommand.validateCommand() {
            UserManager.signin(loginCommand: loginCommand,
                               onSuccess: { (user: User) -> () in
                                self.setUserPreferences(currentUser: user)
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
    
    /* Performs Sign In with Facebook */
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        UserManager.sharedInstance.fetchFacebookProfile(
            onSuccess: {(fbProfile: FacebookProfile) -> () in
                // TODO: Barista Sign In
        }, onError: {(error: String) -> () in
            print(error)
        })
    }
    
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        // FBSDKLoginButtonDelegate says I must implement this method
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
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return identifier == "PerformSignIn" ? performSignIn : true
    }
    
}

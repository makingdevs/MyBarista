//
//  ViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import FBSDKLoginKit
import FBSDKCoreKit

class ViewController: UIViewController {
    
    var performSignIn: Bool = false
    
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var facebookLoginButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        usernameField.underlined()
        usernameField.attributedPlaceholder = NSAttributedString(string: "Usuario",
                                                                 attributes: [NSAttributedStringKey.foregroundColor: UIColor.lightGray])
        passwordField.underlined()
        passwordField.attributedPlaceholder = NSAttributedString(string: "Contraseña",
                                                                 attributes: [NSAttributedStringKey.foregroundColor: UIColor.lightGray])
        facebookLoginButton.layer.cornerRadius = 10
        facebookLoginButton.clipsToBounds = true
        loginButton.layer.cornerRadius = 10
        loginButton.clipsToBounds = true
    }
    
    func getFBUserData(){
        if((FBSDKAccessToken.current()) != nil){
            UserManager.sharedInstance.fetchFacebookProfile(
                onSuccess: {(fbProfile: FacebookProfile) -> () in
                    guard let firstName = fbProfile.firstName,
                        let lastName = fbProfile.lastName,
                        let email = fbProfile.email,
                        let id = fbProfile.id else{
                            
                            return
                    }
                    print ("LOGIN FACE" )
                    let username = firstName + lastName
                    let loginCommand = LoginCommand(username: username,
                                                    password: String(id),
                                                    email: email,
                                                    token: String(describing: FBSDKAccessToken.current()),
                                                    firstName: firstName,
                                                    lastName: lastName)
                    
                    self.fetchUserData(loginCommand: loginCommand)
            }, onError: {(error: String) -> () in
                print(error)
            })
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if isUserLogged(){
            self.perfomSignIn()
        }
    }
    
    @IBAction func signInWithUsername(_ sender: UIButton) {
      let username: String = usernameField.text!
      let password: String = passwordField.text!
        let loginCommand = LoginCommand(username: username, password: password)
        fetchUserData(loginCommand: loginCommand)
    }
    
    @IBAction func loginFacebookAction(_ sender: Any) {
        let fbLoginManager : FBSDKLoginManager = FBSDKLoginManager()
        fbLoginManager.logIn(withReadPermissions: ["email","public_profile"], from: self) { (result, error) -> Void in
            if (error == nil){
                let fbloginresult : FBSDKLoginManagerLoginResult = result!
                // if user cancel the login
                if (result?.isCancelled)!{
                    return
                }
                if(fbloginresult.grantedPermissions.contains("email"))
                {
                    self.getFBUserData()
                }
            }
        }
    }
    
    fileprivate func perfomSignIn() {
        self.performSignIn = true
        self.performSegue(withIdentifier: "PerformSignIn", sender: self)
    }
    
    func fetchUserData(loginCommand: LoginCommand) {
        if loginCommand.validateCommand() {
            UserManager.signin(loginCommand: loginCommand,
                               onSuccess: { (user: User) -> () in
                                self.setUserPreferences(currentUser: user)
                                self.perfomSignIn()
            },
                               onError:{ (error: String) -> () in
                                self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        } else {
          if let errorMessage = loginCommand.errorMessage {
            self.present(self.showErrorAlert(message:errorMessage), animated: true)
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
    
    func isUserLogged() -> Bool{
        let userPreferences = UserDefaults.standard
        if (userPreferences.value(forKey: "currentUser") != nil) &&
            (userPreferences.value(forKey: "currentUserId") != nil) &&
            (userPreferences.value(forKey: "currentUserPassword") != nil){
            return true;
        }
        return false;
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let backItem = UIBarButtonItem()
        backItem.title = ""
        navigationItem.backBarButtonItem = backItem
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return identifier == "PerformSignIn" ? performSignIn : true
    }
    
}

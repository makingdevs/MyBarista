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
    
    let activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView();
    
    var keyboardHeight: CGFloat!
    var distanceToBottom : CGFloat!
    
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
        
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)

        
        self.hideKeyboardWhenTappedAround()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.removeObserver(self)
    }
    
    @IBAction func sendRequest(_ sender: UIButton) {
        let email: String = emailField.text!
        let username: String = usernameField.text!
        let password: String = passwordField.text!
        let confirm: String = confirmField.text!
        
        registrationCommand = RegistrtionCommand(username: username, password: password, confirmPassword: confirm, email: email)
        
        if registrationCommand.validateCommand() {
            self.startLoading()
            UserManager.signup(registrationCommand: registrationCommand,
                               onSuccess: { (user: User) -> () in
                                self.stopLoading()
                                self.setUserPreferences(currentUser: user)
                                _ = self.navigationController?.popViewController(animated: true)
                },
                               onError: { (error: String) -> () in
                                self.stopLoading()
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
    
    func startLoading(){
        activityIndicator.center = self.view.center;
        activityIndicator.hidesWhenStopped = true;
        activityIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray;
        self.view.addSubview(activityIndicator);
        self.view.bringSubview(toFront: activityIndicator)
        activityIndicator.startAnimating();
        UIApplication.shared.beginIgnoringInteractionEvents();
        
    }
    
    func stopLoading(){
        activityIndicator.stopAnimating();
        UIApplication.shared.endIgnoringInteractionEvents();
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if keyboardHeight != nil {
            return
        }
        
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            keyboardHeight = keyboardSize.height
            let globalPoint = signUpButton!.superview?.convert(signUpButton!.frame.origin, to: nil)
            distanceToBottom = (globalPoint?.y)! - self.view.frame.size.height + keyboardHeight + ( 2 * signUpButton.frame.size.height)
            UIView.animate(withDuration: 0.3, animations: {
                if self.view.frame.origin.y == 0{
                    self.view.frame.origin.y -= self.distanceToBottom
                }
            })
        }
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        UIView.animate(withDuration: 0.3) {
            if self.view.frame.origin.y != 0{
                self.view.frame.origin.y += self.distanceToBottom
            }
        }
        
        keyboardHeight = nil
    }
}

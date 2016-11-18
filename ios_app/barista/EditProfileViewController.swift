//
//  EditProfileViewController.swift
//  barista
//
//  Created by MakingDevs on 18/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class EditProfileViewController: UIViewController {
    
    
    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var lastNameField: UITextField!
    
    var updateUserCommand: UpdateUserCommand!
    var userId: Int!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func sendProfileData(_ sender: UIButton) {
        initUserCommand()
        if updateUserCommand.validateCommand() {
            UserManager.updateProfile(
                userCommand: updateUserCommand,
                onSucces: {(userProfile: UserProfile) -> () in
                    print(userProfile.name!)
                    print(userProfile.lastName!)
                },
                onError: {(error: String) -> () in
                    print(error)
            })
        }
    }
    
    func initUserCommand() {
        let name: String = nameField.text!
        let lastName: String = lastNameField.text!
        self.updateUserCommand = UpdateUserCommand(id: userId, name: name, lastName: lastName)
    }
}

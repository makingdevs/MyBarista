//
//  EditProfileViewController.swift
//  barista
//
//  Created by MakingDevs on 18/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

protocol ProfileDelegate {
    func updateProfile(userProfile: UserProfile)
}

class EditProfileViewController: UIViewController {
    
    
    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var lastNameField: UITextField!
    
    var updateUserCommand: UpdateUserCommand!
    var profileDelegate: ProfileDelegate?
    var userId: Int!
    var userProfile: UserProfile!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initProfileForm()
    }
    
    func initProfileForm() {
        nameField.text = userProfile.name
        lastNameField.text = userProfile.lastName
    }
    
    @IBAction func sendProfileData(_ sender: UIButton) {
        initUserCommand()
        if updateUserCommand.validateCommand() {
            UserManager.updateProfile(
                userCommand: updateUserCommand,
                onSucces: {(userProfile: UserProfile) -> () in
                    self.profileDelegate?.updateProfile(userProfile: userProfile)
                    _ = self.navigationController?.popViewController(animated: true)
                },
                onError: {(error: String) -> () in
                    print(error)
            })
        }
    }
    
    func initUserCommand() {
        let name: String = nameField.text!
        let lastName: String = lastNameField.text!
        self.updateUserCommand = UpdateUserCommand(id: userProfile.id!,
                                                   name: name,
                                                   lastName: lastName)
    }
}

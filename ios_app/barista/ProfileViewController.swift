//
//  ProfileViewController.swift
//  barista
//
//  Created by MakingDevs on 17/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {
    
    var username: String!
    var userId: Int!

    override func viewDidLoad() {
        super.viewDidLoad()
        fetchUserPreferences()
        showUserProfile(id: self.userId)
    }
    
    func showUserProfile(id: Int) {
        UserManager.fetchProfile(
            userId: id,
            onSuccess: {(user: UserProfile) -> () in
                // Something
            },
            onError: {(error: String) -> () in
                // Something
        })
    }
    
    func fetchUserPreferences() {
        let userPreferences = UserDefaults.standard
        self.username = userPreferences.string(forKey: "currentUser")
        self.userId = userPreferences.integer(forKey: "currentUserId")
    }
}

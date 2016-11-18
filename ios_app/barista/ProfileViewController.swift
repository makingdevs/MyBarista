//
//  ProfileViewController.swift
//  barista
//
//  Created by MakingDevs on 17/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {
    
    @IBOutlet weak var blurAvatarImageView: UIImageView!
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var coffeeCountLabel: UILabel!
    @IBOutlet weak var webProfileLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var lastNameLabel: UILabel!
    
    var username: String!
    var userId: Int!

    override func viewDidLoad() {
        super.viewDidLoad()
        fetchUserPreferences()
        fetchUserProfile(id: self.userId)
    }
    
    func fetchUserProfile(id: Int) {
        UserManager.fetchProfile(
            userId: id,
            onSuccess: {(user: UserProfile) -> () in
                self.showUserProfile(currentUser: user)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    func showUserProfile(currentUser: UserProfile) {
        avatarImageView.circleFrame()
        blurAvatarImageView.loadURL(url: (currentUser.s3asset?.urlFile)!)
        avatarImageView.loadURL(url: (currentUser.s3asset?.urlFile)!)
        usernameLabel.text = currentUser.username
        coffeeCountLabel.text = String(describing: currentUser.checkinsCount!)
        webProfileLabel.text = "http://users.barist.coffee/#profile/\(currentUser.username!)"
        nameLabel.text = currentUser.name
        lastNameLabel.text = currentUser.lastName
    }
    
    func fetchUserPreferences() {
        let userPreferences = UserDefaults.standard
        self.username = userPreferences.string(forKey: "currentUser")
        self.userId = userPreferences.integer(forKey: "currentUserId")
    }
}

//
//  ProfileViewController.swift
//  barista
//
//  Created by MakingDevs on 17/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import FBSDKLoginKit

class ProfileViewController: UIViewController, ProfileDelegate {
    
    @IBOutlet weak var blurAvatarImageView: UIImageView!
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var coffeeCountLabel: UILabel!
    @IBOutlet weak var webProfileLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    
    var userProfile: UserProfile?
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
                self.userProfile = user
                self.showUserProfile(currentUser: user)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    func showUserProfile(currentUser: UserProfile) {
        nameLabel.text = "\(currentUser.name!) \(currentUser.lastName!)"
        usernameLabel.text = "@\(currentUser.username!)"
        coffeeCountLabel.text = "\(currentUser.checkinsCount!) Cafés"
        webProfileLabel.text = "http://users.barist.coffee/#profile/\(currentUser.username!)"
        if currentUser.s3asset != nil {
            blurAvatarImageView.loadUrlWithBlur(url: (currentUser.s3asset?.urlFile)!)
            avatarImageView.loadAvatarWithBorder(url: (currentUser.s3asset?.urlFile)!)
        }
    }
    
    func updateProfile(profileUpdated: UserProfile) {
        self.userProfile = profileUpdated
        showUserProfile(currentUser: profileUpdated)
    }
    
    func fetchUserPreferences() {
        let userPreferences = UserDefaults.standard
        self.username = userPreferences.string(forKey: "currentUser")
        self.userId = userPreferences.integer(forKey: "currentUserId")
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "performProfileUpdate" {
            let editProfileViewController = segue.destination as! EditProfileViewController
            editProfileViewController.profileDelegate = self
            editProfileViewController.userProfile = self.userProfile
        }
    }
    
    @IBAction func signOutUser(_ sender: Any) {
        let userPreferences = UserDefaults.standard
        userPreferences.set(nil, forKey: "currentUser")
        userPreferences.set(nil, forKey: "currentUserId")
        userPreferences.set(nil, forKey: "currentUserPassword")
        userPreferences.synchronize()
        let loginManager = FBSDKLoginManager()
        loginManager.logOut() // this is an instance function
        self.dismiss(animated: true, completion: nil)

    }
    
    
}

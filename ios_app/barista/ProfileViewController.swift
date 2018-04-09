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
    @IBOutlet weak var editButton: UIButton!
    @IBOutlet weak var closeSessionButton: UIButton!
  
    var userProfile: UserProfile?
    var username: String!
    var userId: Int!
    
    let activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fetchUserPreferences()
        editButton.bordered()
        let orange = UIColor(red: 234.0/255.0, green: 93.0/255.0, blue: 47.0/255.0, alpha: 0.8)
        closeSessionButton.bordered(with: orange)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        fetchUserProfile(id: self.userId)
    }
    
    func fetchUserProfile(id: Int) {
        self.startLoading()
        UserManager.fetchProfile(
            userId: id,
            onSuccess: {(user: UserProfile) -> () in
                self.stopLoading()
                self.userProfile = user
                self.showUserProfile(currentUser: user)
            },
            onError: {(error: String) -> () in
                self.stopLoading()
                print(error)
        })
    }
    
    func showUserProfile(currentUser: UserProfile) {
        nameLabel.text = "\(currentUser.name ?? "") \(currentUser.lastName ?? "")"
        usernameLabel.text = "@\(currentUser.username ?? "")"
        coffeeCountLabel.text = "\(currentUser.checkinsCount ?? 0) Cafés"
        webProfileLabel.text = "http://users.barist.coffee/#profile/\(currentUser.username ?? "")"
        if currentUser.s3asset != nil {
            if let urlFile = currentUser.s3asset?.urlFile {
                blurAvatarImageView.loadUrlWithBlur(url: urlFile)
                avatarImageView.loadAvatarWithBorder(url: urlFile)
            }
        }
    }
    
    func updateProfile(profileUpdated: UserProfile) {
        self.userProfile = profileUpdated
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
            let backItem = UIBarButtonItem()
            backItem.title = ""
            navigationItem.backBarButtonItem = backItem
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
}

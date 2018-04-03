//
//  EditProfileViewController.swift
//  barista
//
//  Created by MakingDevs on 18/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import ImagePicker

protocol ProfileDelegate {
    func updateProfile(profileUpdated: UserProfile)
}

class EditProfileViewController: UIViewController, UINavigationControllerDelegate, ImagePickerDelegate {
    
    
    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var lastNameField: UITextField!
    @IBOutlet weak var userPhotoImageView: UIImageView!
    @IBOutlet weak var changePhotoButton: UIButton!
    @IBOutlet weak var saveButton: UIButton!
    
    var updateUserCommand: UpdateUserCommand!
    var profileDelegate: ProfileDelegate?
    var userId: Int!
    var userProfile: UserProfile!
    var userImage: UIImage?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        nameField.underlined()
        lastNameField.underlined()
        saveButton.bordered()
        changePhotoButton.bordered()
        self.hideKeyboardWhenTappedAround()
        initProfileForm()
    }
    
    func initProfileForm() {
        nameField.text = userProfile.name
        lastNameField.text = userProfile.lastName
        if userProfile.s3asset != nil {
            userPhotoImageView.loadAvatar(url: (userProfile.s3asset?.urlFile)!)
        }
    }
    
  @IBAction func sendProfileData() {
        initUserCommand()
        if updateUserCommand.validateCommand() {
            UserManager.updateProfile(
                userCommand: updateUserCommand,
                onSucces: {(userProfile: UserProfile) -> () in
                    self.profileDelegate?.updateProfile(profileUpdated: userProfile)
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
    
    @IBAction func changeUserPhoto(_ sender: Any?) {
        var config = Configuration()
        config.doneButtonTitle = "OK"
        config.noImagesTitle = "Lo sentimos, no hay imagenes disponibles"
        config.recordLocation = false
        config.allowVideoSelection = false
        
        let imagePicker = ImagePickerController(configuration: config)
        imagePicker.imageLimit = 1
        imagePicker.delegate = self
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        self.userImage = info[UIImagePickerControllerOriginalImage] as? UIImage
        userPhotoImageView.image = self.userImage
        picker.dismiss(animated: true, completion: {
            self.uploadAsset()
        })
    }
    
    func uploadAsset(){
        guard let userProfileId = userProfile.id, let userImage = self.userImage else{
            print("There are not values for id and image")
            return
        }
        
        let uploadCommand = UploadCommand(userId: userProfileId, image: userImage)
        S3AssetManager.uploadUserPhoto(
            uploadCommand: uploadCommand,
            onSuccess: {(userPhoto: PhotoCheckin) -> () in
                self.userProfile.s3asset?.urlFile = userPhoto.urlFile
                self.profileDelegate?.updateProfile(profileUpdated: self.userProfile)
                _ = self.navigationController?.popViewController(animated: true)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    // MARK: - ImagePickerDelegate
    func cancelButtonDidPress(_ imagePicker: ImagePickerController) {
        imagePicker.dismiss(animated: true, completion: nil)
    }
    
    func wrapperDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
        guard images.count > 0 else { return }
        self.userImage = images[0]
        userPhotoImageView.image = self.userImage
        imagePicker.dismiss(animated: true, completion: {
            self.uploadAsset()
        })
    }
    
    func doneButtonDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
        guard images.count > 0 else { return }
        self.userImage = images[0]
        userPhotoImageView.image = self.userImage
        imagePicker.dismiss(animated: true, completion: {
            self.uploadAsset()
        })
    }
}

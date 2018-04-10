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

class EditProfileViewController: UIViewController, UINavigationControllerDelegate, ImagePickerDelegate, UITextFieldDelegate {
    
    
    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var lastNameField: UITextField!
    @IBOutlet weak var userPhotoImageView: UIImageView!
    @IBOutlet weak var changePhotoButton: UIButton!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!
    
    @IBOutlet weak var constraintContentHeight: NSLayoutConstraint!
    
    var updateUserCommand: UpdateUserCommand!
    var profileDelegate: ProfileDelegate?
    var userId: Int!
    var userProfile: UserProfile!
    var userImage: UIImage?
    
    var activeField: UITextField?
    var lastOffset: CGPoint!
    var keyboardHeight: CGFloat!
    
    let activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        nameField.underlined()
        lastNameField.underlined()
        saveButton.bordered()
        changePhotoButton.bordered()
        self.hideKeyboardWhenTappedAround()
        initProfileForm()
        nameField.delegate = self
        lastNameField.delegate = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
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
            self.startLoading()
            UserManager.updateProfile(
                userCommand: updateUserCommand,
                onSucces: {(userProfile: UserProfile) -> () in
                    self.stopLoading()
                    self.profileDelegate?.updateProfile(profileUpdated: userProfile)
                    _ = self.navigationController?.popViewController(animated: true)
                },
                onError: {(error: String) -> () in
                    self.stopLoading()
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
        self.startLoading()
        S3AssetManager.uploadUserPhoto(
            uploadCommand: uploadCommand,
            onSuccess: {(userPhoto: PhotoCheckin) -> () in
                self.stopLoading()
                self.userProfile.s3asset?.urlFile = userPhoto.urlFile
                self.profileDelegate?.updateProfile(profileUpdated: self.userProfile)
                _ = self.navigationController?.popViewController(animated: true)
            },
            onError: {(error: String) -> () in
                self.stopLoading()
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
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        activeField = textField
        lastOffset = self.scrollView.contentOffset
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        activeField?.resignFirstResponder()
        activeField = nil
        return true
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        print("keyboardWillShow")
        if keyboardHeight != nil {
            return
        }
        
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            keyboardHeight = keyboardSize.height
            
            // so increase contentView's height by keyboard height
            UIView.animate(withDuration: 0.3, animations: {
                print("aniamting 111")
                self.constraintContentHeight.constant += self.keyboardHeight
            })
            
            let globalPoint = activeField!.superview?.convert(activeField!.frame.origin, to: nil)
            
            // move if keyboard hide input field
            let distanceToBottom = 46 + self.scrollView.frame.size.height - (globalPoint?.y)! - (activeField?.frame.size.height)!
            let collapseSpace = keyboardHeight - distanceToBottom
            if collapseSpace < 0 {
                // no collapse
                return
            }
            
            // set new offset for scroll view
            UIView.animate(withDuration: 0.3, animations: {
                // scroll to the position above keyboard 10 points

                self.scrollView.contentOffset = CGPoint(x: self.lastOffset.x, y: collapseSpace + 20)
            })
        }
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        print("keyboardWillHide")
        UIView.animate(withDuration: 0.3) {
            self.constraintContentHeight.constant -= self.keyboardHeight
            
            self.scrollView.contentOffset = self.lastOffset
        }
        
        keyboardHeight = nil
    }
}

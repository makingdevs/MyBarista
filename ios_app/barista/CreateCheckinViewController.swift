//
//  CreateCheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CreateCheckinViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    @IBOutlet weak var checkinPhoto: UIImageView!
    @IBOutlet weak var methodField: UITextField!
    @IBOutlet weak var stateField: UITextField!
    @IBOutlet weak var originField: UITextField!
    @IBOutlet weak var priceField: UITextField!
    @IBOutlet weak var venueLabel: UILabel!
    
    let methodPickerView = UIPickerView()
    let statePickerView = UIPickerView()
    var methodList = ["Expresso", "Americano", "Goteo", "Prensa", "Sifón", "Otro"]
    var stateList = ["Veracruz", "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Otro"]
    
    let userPreferences = UserDefaults.standard
    var checkinCommand: CheckinCommand!
    var uploadCommand: UploadCommand!
    var method: String!
    var state: String!
    var origin: String!
    var price: String!
    var image: UIImage!

    override func viewDidLoad() {
        super.viewDidLoad()
        initPickerViews()
        method = methodList[0]
        state = stateList[0]
        let venueAction = UITapGestureRecognizer(target: self, action: #selector(CreateCheckinViewController.selectVenue))
        venueLabel.addGestureRecognizer(venueAction)
    }
    
    func selectVenue() {
        let venueController: VenueTableViewController = VenueTableViewController()
        self.navigationController?.pushViewController(venueController, animated: true)
    }
    
    @IBAction func createCheckin(_ sender: UIBarButtonItem) {
        // Improve this piece of code
        if let action = sender.title {
            switch action {
            case "Done":
                if image != nil {
                    uploadCommand = UploadCommand(image: image)
                    S3AssetManager.uploadCheckinPhoto(
                        uploadCommand: uploadCommand,
                        onPhotoSuccess: { (photoCheckin: PhotoCheckin) -> () in
                            self.getCheckInForm(assetId: photoCheckin.id)
                        },
                        onPhotoError: { (error: String) -> () in
                            print(error.description)
                    })
                } else {
                    getCheckInForm(assetId: nil)
                }
            default:
                _ = self.tabBarController?.selectedIndex = 0
            }
        }
    }
    
    func getCheckInForm(assetId: Int?){
        price = priceField.text!
        origin = originField.text!
        checkinCommand = CheckinCommand(username: userPreferences.string(forKey: "currentUser")!, method: method, state: state, origin: origin, price: price, idS3Asset: assetId, created_at: Date())
        if checkinCommand.validateCommand() {
            CheckinManager.create(
                checkinCommand: checkinCommand,
                onSuccess: { (checkin: Checkin) -> () in
                    _ = self.tabBarController?.selectedIndex = 0
                },
                onError: { (error: String) -> () in
                    print(error.description)
            })
        }
    }
    
    @IBAction func selectPicture(_ sender: UIBarButtonItem) {
        let checkInImagePicker = UIImagePickerController()
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            checkInImagePicker.sourceType = .camera
        } else {
            checkInImagePicker.sourceType = .photoLibrary
        }
        checkInImagePicker.delegate = self
        present(checkInImagePicker, animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        self.image = info[UIImagePickerControllerOriginalImage] as! UIImage!
        checkinPhoto.image = self.image
        dismiss(animated: true, completion: nil)
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == methodPickerView {
            return methodList.count
        } else {
            return stateList.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == methodPickerView {
            return methodList[row]
        } else {
            return stateList[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == methodPickerView {
            method = methodList[row]
            methodField.text = methodList[row]
        } else {
            state = stateList[row]
            stateField.text = stateList[row]
        }
        self.view.endEditing(true)
    }
    
    func initPickerViews() {
        methodPickerView.delegate = self
        statePickerView.delegate = self
        methodPickerView.backgroundColor = UIColor.white
        statePickerView.backgroundColor = UIColor.white
        methodField.inputView = methodPickerView
        stateField.inputView = statePickerView
        methodField.text = methodList[0]
        stateField.text = stateList[0]
    }
}

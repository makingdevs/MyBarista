//
//  CreateCheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CreateCheckinViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UINavigationControllerDelegate, UIImagePickerControllerDelegate, VenueDelegate {
    
    @IBOutlet weak var checkinPhoto: UIImageView!
    @IBOutlet weak var methodField: UITextField!
    @IBOutlet weak var stateField: UITextField!
    @IBOutlet weak var originField: UITextField!
    @IBOutlet weak var priceField: UITextField!
    @IBOutlet weak var venueLabel: UIButton!
    
    let methodPickerView = UIPickerView()
    let statePickerView = UIPickerView()
    var methodList = ["Expresso", "Americano", "Goteo", "Prensa", "Sifón", "Otro"]
    var stateList = ["Veracruz", "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Otro"]
    
    let userPreferences = UserDefaults.standard
    var uploadCommand: UploadCommand!
    var checkInAction: String = "CREATE"
    var checkin: Checkin?
    var method: String!
    var state: String!
    var origin: String!
    var price: String!
    var image: UIImage?
    var venue: String?

    override func viewDidLoad() {
        super.viewDidLoad()
        initPickerViews()
        setCurrentCheckIn()
        method = methodList[0]
        state = stateList[0]
    }
    
    func setCurrentCheckIn() {
        checkinPhoto.loadURL(url: checkin != nil ? (checkin?.s3Asset?.urlFile)! : "", placeholder: #imageLiteral(resourceName: "coffee_holder"))
        methodField.text = checkin?.method
        stateField.text = checkin?.state
        originField.text = checkin?.origin
        priceField.text = checkin?.price
        venueLabel.setTitle(!(checkin?.venue?.isEmpty)! ? checkin?.venue : "Agrega un lugar", for: .normal)
    }
    
    func updateVenueName(venueSelected: Venue) {
        self.venue = venueSelected.id
        self.venueLabel.setTitle(venueSelected.name, for: .normal)
    }
    
    @IBAction func createCheckin(_ sender: UIBarButtonItem) {
        // Improve this piece of code
        if let action = sender.title {
            switch action {
            case "Done":
                uploadCommand = UploadCommand(image: image)
                if uploadCommand.validateCommand() {
                    S3AssetManager.uploadCheckinPhoto(
                        uploadCommand: uploadCommand,
                        onPhotoSuccess: { (photoCheckin: PhotoCheckin) -> () in
                            self.getCheckInForm(asset: photoCheckin.id)
                        },
                        onPhotoError: { (error: String) -> () in
                            print(error.description)
                    })
                } else {
                    getCheckInForm(asset: nil)
                }
            default:
                _ = self.tabBarController?.selectedIndex = 0
            }
        }
    }
    
    func getCheckInForm(asset: Int?){
        price = priceField.text!
        origin = originField.text!
        let checkinCommand: CheckinCommand = CheckinCommand(username: userPreferences.string(forKey: "currentUser")!, method: method, state: state, origin: origin, price: price, idS3Asset: asset, idVenueFoursquare: venue, created_at: checkInAction == "CREATE" ? Date() : (checkin?.createdAt)!)
        if checkinCommand.validateCommand() {
            switch checkInAction {
            case "CREATE":
                CheckinManager.create(
                    checkinCommand: checkinCommand,
                    onSuccess: { (checkin: Checkin) -> () in
                        _ = self.tabBarController?.selectedIndex = 0
                    },
                    onError: { (error: String) -> () in
                        self.present(self.showErrorAlert(message: error.description), animated: true)
                })
            case "UPDATE":
                CheckinManager.update(
                    checkinId: checkin?.id,
                    checkinCommand: checkinCommand,
                    onSuccess: { (checkin: Checkin) -> () in
                        print(checkin)
                    },
                    onError: { (error: String) -> () in
                        print(error.description)
                })
            default:
                break
            }
                    } else {
            self.present(self.showErrorAlert(message: checkinCommand.errorMessage!), animated: true)
        }
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Algo salió mal", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "performSelectVenue" {
            let venueTableViewController = segue.destination as! VenueTableViewController
            venueTableViewController.delegate = self
        }
    }
}

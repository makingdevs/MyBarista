//
//  CreateCheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

protocol CheckinDelegate {
    func updateCheckinDetail(currentCheckin: Checkin)
}

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
    var checkinCommand: CheckinCommand!
    var checkinDelegate: CheckinDelegate?
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
        methodField.underlined()
        stateField.underlined()
        originField.underlined()
        priceField.underlined()
        
        self.hideKeyboardWhenTappedAround()
        initPickerViews()
        method = methodList[0]
        state = stateList[0]
        if checkin != nil {
            setCurrentCheckIn()
        }
    }
    
    func cleanView(){
        methodField.text = methodList[0]
        stateField.text = stateList[0]
        originField.text = ""
        priceField.text = ""
        venueLabel.setTitle("Agrega un lugar", for: .normal)
        checkin?.method = nil
        checkin?.state = nil
        checkin?.origin = nil
        checkin?.price = nil
        checkin?.venue = nil
        checkin?.s3Asset = nil
        checkinPhoto.image =  UIImage(named: "coffee_holder")
    }
    
    func setCurrentCheckIn() {
        methodField.text = checkin?.method
        stateField.text = checkin?.state
        originField.text = checkin?.origin
        priceField.text = checkin?.price
        if let venue = checkin?.venue, venue != "" {
            venueLabel.setTitle( checkin?.venue, for: .normal)
        }else{
            venueLabel.setTitle( "Agrega un lugar", for: .normal)
        }
      
        if let venueId = checkin?.venueId {
            self.venue = "\(venueId)"
        }
      
        if let s3Asset = checkin?.s3Asset, let urlFile = s3Asset.urlFile {
            checkinPhoto.loadURL(url: urlFile)
        }
    }
    
    func updateVenueName(venueSelected: Venue) {
        self.venue = venueSelected.id
        self.venueLabel.setTitle(venueSelected.name, for: .normal)
    }
    
    @IBAction func createCheckin(_ sender: UIBarButtonItem) {
        // Improve this piece of code
        if let action = sender.title {
            switch action {
            case "Guardar":
                getCheckInForm(asset: nil)
                uploadCommand = UploadCommand(image: image)
                if uploadCommand.validateCommand() {
                    S3AssetManager.uploadCheckinPhoto(
                        uploadCommand: uploadCommand,
                        onPhotoSuccess: { (photoCheckin: PhotoCheckin?) -> () in
                            if let checkinId = photoCheckin?.id {
                                self.saveCheckin(asset: checkinId)
                            }else{
                                 self.present(self.showErrorAlert(message: "Error al guardar la imagen"), animated: true)
                            }
                        },
                        onPhotoError: { (error: String) -> () in
                            print("Photo: \(error.description)")
                    })
                } else {
                    if checkin?.s3Asset != nil {
                        saveCheckin(asset: checkin?.s3Asset?.id)
                    } else {
                        saveCheckin(asset: nil)
                    }
                }
            default:
                if checkInAction == "CREATE" {
                    _ = self.tabBarController?.selectedIndex = 0
                } else {
                    _ = self.navigationController?.popViewController(animated: true)
                }
            }
        }
    }
    
    func saveCheckin (asset: Int?) {
        getCheckInForm(asset: asset)
        switch checkInAction {
        case "CREATE":
            CheckinManager.create(
                checkinCommand: checkinCommand,
                onSuccess: { (checkin: Checkin) -> () in
                    print("save ending")
                    self.cleanView()
                    _ = self.tabBarController?.selectedIndex = 0
                },
                onError: { (error: String) -> () in
                    self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        case "UPDATE":
            guard let checkinId = checkin?.id else{
                return
            }
            
            CheckinManager.update(
                checkinId: checkinId,
                checkinCommand: checkinCommand,
                onSuccess: { (checkin: Checkin) -> () in
                    self.checkinDelegate?.updateCheckinDetail(currentCheckin: checkin)
                    _ = self.navigationController?.popViewController(animated: true)
                },
                onError: { (error: String) -> () in
                    self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        default:
            break
        }
    }
    
    func getCheckInForm(asset: Int?) {
        price = priceField.text!
        origin = originField.text!
        var asignedDate = Date()
        if checkInAction == "CREATE", let createdAt = checkin?.createdAt{
            asignedDate = createdAt
        }
        if let currentUser = userPreferences.string(forKey: "currentUser") {
            checkinCommand = CheckinCommand(username: currentUser,
                                        method: method,
                                        state: state,
                                        origin: origin,
                                        price: price,
                                        idS3Asset: asset,
                                        idVenueFoursquare: venue,
                                        created_at: asignedDate)
        }
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Algo salió mal", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
    }
    
    @IBAction func selectPicture(_ sender: Any?) {
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
            venueTableViewController.venueDelegate = self
            let backItem = UIBarButtonItem()
            backItem.title = ""
            navigationItem.backBarButtonItem = backItem
        }
    }
}

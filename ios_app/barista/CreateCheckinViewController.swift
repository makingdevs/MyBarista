//
//  CreateCheckinViewController.swift
//  barista
//
//  Created by Ariana Santillán on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CreateCheckinViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    
    @IBOutlet weak var methodField: UITextField!
    @IBOutlet weak var stateField: UITextField!
    @IBOutlet weak var originField: UITextField!
    @IBOutlet weak var venueField: UITextField!
    @IBOutlet weak var priceField: UITextField!
    
    let methodPickerView = UIPickerView()
    let statePickerView = UIPickerView()
    var methodList = ["Expresso", "Americano", "Goteo", "Prensa", "Sifón", "Otro"]
    var stateList = ["Veracruz", "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Otro"]
    
    let userPreferences = UserDefaults.standard
    var checkinCommand: CheckinCommand!
    var method: String!
    var state: String!
    var origin: String!
    var price: String!

    override func viewDidLoad() {
        super.viewDidLoad()
        initPickerViews()
        method = methodList[0]
        state = stateList[0]
    }
    
    @IBAction func createCheckin(_ sender: UIBarButtonItem) {
        if let action = sender.title {
            if action == "Done" {
                price = priceField.text!
                origin = priceField.text!
                
                checkinCommand = CheckinCommand(username: userPreferences.string(forKey: "currentUser")!,
                                                method: method,
                                                state: state,
                                                origin: origin,
                                                price: price,
                                                created_at: Date())
                
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
            } else {
                _ = self.tabBarController?.selectedIndex = 0
            }
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

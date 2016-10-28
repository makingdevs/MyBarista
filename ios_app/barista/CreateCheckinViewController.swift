//
//  CreateCheckinViewController.swift
//  barista
//
//  Created by Ariana Santillán on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CreateCheckinViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    
    @IBOutlet weak var methodField: UITextField!
    @IBOutlet weak var stateField: UITextField!
    @IBOutlet weak var originField: UITextField!
    @IBOutlet weak var venueField: UITextField!
    @IBOutlet weak var priceField: UITextField!
    
    let methodPickerView = UIPickerView()
    let statePickerView = UIPickerView()
    var methodList = ["Expreso", "Americano", "Goteo", "Prensa", "Sifón", "Otro"]
    var stateList = ["Veracruz", "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Otro"]

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        methodPickerView.delegate = self
        statePickerView.delegate = self
        methodPickerView.showsSelectionIndicator = true
        methodPickerView.showsSelectionIndicator = true
        methodPickerView.backgroundColor = UIColor.white
        statePickerView.backgroundColor = UIColor.white
        methodField.inputView = methodPickerView
        stateField.inputView = statePickerView
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
            methodField.text = methodList[row]
        } else {
            stateField.text = stateList[row]
        }
        self.view.endEditing(true)
    }
}

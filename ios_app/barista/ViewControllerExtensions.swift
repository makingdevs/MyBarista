//
//  ViewControllerExtensions.swift
//  barista
//
//  Created by Ariana Santillán on 07/12/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import UIKit

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap : UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dissmisKeyboard))
        view.addGestureRecognizer(tap)
    }
    
    func dissmisKeyboard() {
        view.endEditing(true)
    }
}

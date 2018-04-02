//
//  UIButtonExtentions.swift
//  barista
//
//  Created by marco reyes on 30/03/18.
//  Copyright © 2018 MakingDevs. All rights reserved.
//

import UIKit

extension UIButton {
  func bordered(){
    self.backgroundColor = .clear
    self.layer.cornerRadius = 5
    self.layer.borderWidth = 1
    let coffee = UIColor(red: 145.0/255.0, green: 116.0/255.0, blue: 70.0/255.0, alpha: 1.0)
    self.layer.borderColor = coffee.cgColor
  }
  
  func bordered(with color:UIColor){
    self.backgroundColor = .clear
    self.layer.cornerRadius = 5
    self.layer.borderWidth = 1
    let coffee = color
    self.layer.borderColor = coffee.cgColor
  }
}

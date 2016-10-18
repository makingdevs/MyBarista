//
//  CheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CheckinViewController: UIViewController {
    
    var checkin:Checkin!
    
    @IBOutlet weak var methodLabel: UILabel!
    @IBOutlet weak var originLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var checkinPhotoView: UIImageView!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.title = checkin.method
        methodLabel.text = checkin.method
        originLabel.text = checkin.origin
        priceLabel!.text = "$ \(checkin.price)"
        DispatchQueue.global( qos: DispatchQoS.QoSClass.default).async(execute: {
            DispatchQueue.main.async(execute: {
                let myImage =  UIImage(data: try! Data(contentsOf: URL(string:self.checkin.urlPhoto!)!))
                self.checkinPhotoView.image = myImage
            })
        })
    }
}

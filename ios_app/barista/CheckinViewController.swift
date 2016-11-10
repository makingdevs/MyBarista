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
    @IBOutlet weak var stateLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var checkinPhotoView: UIImageView!
    @IBOutlet weak var venueLabel: UILabel!
    @IBOutlet weak var noteLabel: UILabel!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.title = checkin.method
        showCheckinDetail()
    }
    
    func showCheckinDetail () {
        checkinPhotoView.loadURL(url: (checkin.s3Asset?.urlFile)!, placeholder: #imageLiteral(resourceName: "coffee_holder"))
        methodLabel.text = checkin.method
        stateLabel.text = checkin.state
        priceLabel!.text = "$ \(checkin.price!)"
        venueLabel.text = checkin.venue
        noteLabel.text = checkin.note
    }
}

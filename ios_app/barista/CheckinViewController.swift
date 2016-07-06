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
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        self.title = checkin.method
        methodLabel.text = checkin.method
        originLabel.text = checkin.origin
        priceLabel!.text = "$ \(checkin.price)"
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
            dispatch_async(dispatch_get_main_queue(), {
                let myImage =  UIImage(data: NSData(contentsOfURL: NSURL(string:self.checkin.urlPhoto!)!)!)
                self.checkinPhotoView.image = myImage
            })
        })
    }
}
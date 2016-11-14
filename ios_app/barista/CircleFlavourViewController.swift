//
//  CircleFlavourViewController.swift
//  barista
//
//  Created by MakingDevs on 14/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CircleFlavourViewController: UIViewController {

    var checkin: Checkin!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        showCircleFlavor()
    }
    
    /* Fetchs Check-in circle flavor */
    func showCircleFlavor() {
        CheckinManager.fetchCircleFlavor(
            checkinId: checkin.id,
            onSuccess: {(circleFlavor: CircleFlavor) -> () in
                print(circleFlavor)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
}

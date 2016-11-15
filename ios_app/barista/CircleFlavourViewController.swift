//
//  CircleFlavourViewController.swift
//  barista
//
//  Created by MakingDevs on 14/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CircleFlavourViewController: UIViewController, CircleFlavorDelegate {

    var checkin: Checkin!
    var circleFlavor: CircleFlavor!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        showCircleFlavor()
    }
    
    /* Fetchs Check-in circle flavor */
    func showCircleFlavor() {
        CheckinManager.fetchCircleFlavor(
            circleFlavorId: checkin.circleFlavor!,
            onSuccess: {(circleFlavor: CircleFlavor) -> () in
                self.circleFlavor = circleFlavor
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    func updateCircleFlavor(checkin: Checkin, circleFlavor: CircleFlavor) {
        self.checkin = checkin
        self.circleFlavor = circleFlavor
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let createCircleViewController = segue.destination as! CreateCircleFlavorViewController
        createCircleViewController.checkin = self.checkin
        createCircleViewController.circleFlavor = self.circleFlavor
        createCircleViewController.delegate = self
    }
}

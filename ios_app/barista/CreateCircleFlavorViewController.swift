//
//  CreateCircleFlavorViewController.swift
//  barista
//
//  Created by MakingDevs on 15/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CreateCircleFlavorViewController: UIViewController {

    var checkin: Checkin!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func saveCircleFlavor(_ sender: UIButton) {
        CheckinManager.createCircleFlavor(
            checkinId: checkin.id,
            circleFlavor:  getCircleFlavor(),
            onSuccess: {(checkin: Checkin) -> () in
                print(checkin.circleFlavor)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    func getCircleFlavor() -> CircleFlavor {
        let circleFlavor: CircleFlavor = CircleFlavor(sweetness: 1,
                                                      acidity: 6,
                                                      flowery: 1,
                                                      spicy: 1,
                                                      salty: 1,
                                                      berries: 1,
                                                      chocolate: 1,
                                                      candy: 1,
                                                      body: 1,
                                                      cleaning: 1)
        return circleFlavor
    }
}

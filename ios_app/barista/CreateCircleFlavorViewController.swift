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
    var circleFlavor: CircleFlavor!
    
    @IBOutlet weak var sweetnessLabel: UILabel!
    @IBOutlet weak var acidityLabel: UILabel!
    @IBOutlet weak var floweryLabel: UILabel!
    @IBOutlet weak var spicyLabel: UILabel!
    @IBOutlet weak var saltyLabel: UILabel!
    @IBOutlet weak var berriesLabel: UILabel!
    @IBOutlet weak var chocolateLabel: UILabel!
    @IBOutlet weak var candyLabel: UILabel!
    @IBOutlet weak var bodyLabel: UILabel!
    @IBOutlet weak var cleaningLabel: UILabel!
    
    @IBOutlet weak var sweetnessSlider: UISlider!
    @IBOutlet weak var aciditySlider: UISlider!
    @IBOutlet weak var flowerySlider: UISlider!
    @IBOutlet weak var spicySlider: UISlider!
    @IBOutlet weak var saltySlider: UISlider!
    @IBOutlet weak var berriesSlider: UISlider!
    @IBOutlet weak var chocolateSlider: UISlider!
    @IBOutlet weak var candySlider: UISlider!
    @IBOutlet weak var bodySlider: UISlider!
    @IBOutlet weak var cleaningSlider: UISlider!
    
    var sweetness: Int?
    var acidity: Int?
    var flowery: Int?
    var spicy: Int?
    var salty: Int?
    var berries: Int?
    var chocolate: Int?
    var candy: Int?
    var body: Int?
    var cleaning: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if circleFlavor != nil {
            setCurrentCircleFlavor()
        }
    }
    
    func setCurrentCircleFlavor() {
        sweetnessSlider.setValue(Float(circleFlavor.sweetness!), animated: true)
        sweetnessLabel.text = "Dulzura: \(circleFlavor.sweetness!)"
        aciditySlider.setValue(Float(circleFlavor.acidity!), animated: true)
        acidityLabel.text = "Acidez: \(circleFlavor.acidity!)"
        flowerySlider.setValue(Float(circleFlavor.flowery!), animated: true)
        floweryLabel.text = "Florar: \(circleFlavor.flowery!)"
        spicySlider.setValue(Float(circleFlavor.spicy!), animated: true)
        spicyLabel.text = "Especiado: \(circleFlavor.spicy!)"
        saltySlider.setValue(Float(circleFlavor.salty!), animated: true)
        saltyLabel.text = "Salado: \(circleFlavor.salty!)"
        berriesSlider.setValue(Float(circleFlavor.berries!), animated: true)
        berriesLabel.text = "Frutos Rojos: \(circleFlavor.berries!)"
        chocolateSlider.setValue(Float(circleFlavor.chocolate!), animated: true)
        chocolateLabel.text = "Chocolate: \(circleFlavor.chocolate!)"
        candySlider.setValue(Float(circleFlavor.candy!), animated: true)
        candyLabel.text = "Caramelo: \(circleFlavor.candy!)"
        bodySlider.setValue(Float(circleFlavor.body!), animated: true)
        bodyLabel.text = "Cuerpo: \(circleFlavor.body!)"
        cleaningSlider.setValue(Float(circleFlavor.cleaning!), animated: true)
        cleaningLabel.text = "Limpieza: \(circleFlavor.cleaning!)"
    }

    @IBAction func flavorValueChanged(_ sender: UISlider) {
        if let slider = sender.accessibilityLabel {
            let flavorValue = Int(roundf(sender.value))
            switch slider {
            case "sweetness":
                self.sweetness = flavorValue
                sweetnessLabel.text = "Dulzura: \(flavorValue)"
            case "acidity":
                self.acidity = flavorValue
                acidityLabel.text = "Acidez: \(flavorValue)"
            case "flowery":
                self.flowery = flavorValue
                floweryLabel.text = "Florar: \(flavorValue)"
            case "spicy":
                self.spicy = flavorValue
                spicyLabel.text = "Especiado: \(flavorValue)"
            case "salty":
                self.salty = flavorValue
                saltyLabel.text = "Salado: \(flavorValue)"
            case "berries":
                self.berries = flavorValue
                berriesLabel.text = "Frutos Rojos: \(flavorValue)"
            case "chocolate":
                self.chocolate = flavorValue
                chocolateLabel.text = "Chocolate: \(flavorValue)"
            case "candy":
                self.candy = flavorValue
                candyLabel.text = "Caramelo: \(flavorValue)"
            case "body":
                self.body = flavorValue
                bodyLabel.text = "Cuerpo: \(flavorValue)"
            case "cleaning":
                self.cleaning = flavorValue
                cleaningLabel.text = "Limpieza: \(flavorValue)"
            default:
                break
            }
        }
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
        let circleFlavor: CircleFlavor = CircleFlavor(sweetness: self.sweetness,
                                                      acidity: self.acidity,
                                                      flowery: self.flowery,
                                                      spicy: self.spicy,
                                                      salty: self.salty,
                                                      berries: self.berries,
                                                      chocolate: self.chocolate,
                                                      candy: self.candy,
                                                      body: self.body,
                                                      cleaning: self.cleaning)
        return circleFlavor
    }
}

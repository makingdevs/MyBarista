//
//  CreateCircleFlavorViewController.swift
//  barista
//
//  Created by MakingDevs on 15/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

protocol CircleFlavorDelegate {
    func updateCircleFlavor(checkin: Checkin, circleFlavor: CircleFlavor)
}

class CreateCircleFlavorViewController: UIViewController {

    var checkin: Checkin!
    var circleFlavor: CircleFlavor!
    var circleFlavorDelegate: CircleFlavorDelegate?
    
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
    
    let activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Guardar", style: .plain, target: self, action: #selector(saveCircleFlavor))
        if circleFlavor != nil {
            setCurrentCircleFlavor()
        }
    }
    
    func setCurrentCircleFlavor() {
        if let sweetness = circleFlavor.sweetness {
            sweetnessSlider.setValue(Float(sweetness), animated: true)
            sweetnessLabel.text = "Dulzura: \(sweetness)"
        }
        if let acidity = circleFlavor.acidity {
            aciditySlider.setValue(Float(acidity), animated: true)
            acidityLabel.text = "Acidez: \(acidity)"
        }
        if let flowery = circleFlavor.flowery {
            flowerySlider.setValue(Float(flowery), animated: true)
            floweryLabel.text = "Florar: \(flowery)"
        }
        if let spicy = circleFlavor.spicy {
            spicySlider.setValue(Float(spicy), animated: true)
            spicyLabel.text = "Especiado: \(spicy)"
        }
        if let salty = circleFlavor.salty {
            saltySlider.setValue(Float(salty), animated: true)
            saltyLabel.text = "Salado: \(salty)"
        }
        if let berries = circleFlavor.berries {
            berriesSlider.setValue(Float(berries), animated: true)
            berriesLabel.text = "Frutos Rojos: \(berries)"
        }
        if let chocolate = circleFlavor.chocolate {
            chocolateSlider.setValue(Float(chocolate), animated: true)
            chocolateLabel.text = "Chocolate: \(chocolate)"
        }
        if let candy = circleFlavor.candy {
            candySlider.setValue(Float(candy), animated: true)
            candyLabel.text = "Caramelo: \(candy)"
        }
        if let body = circleFlavor.body {
            bodySlider.setValue(Float(body), animated: true)
            bodyLabel.text = "Cuerpo: \(body)"
        }
        if let cleaning = circleFlavor.cleaning {
            cleaningSlider.setValue(Float(cleaning), animated: true)
            cleaningLabel.text = "Limpieza: \(cleaning)"
        }
    }

    @IBAction func flavorValueChanged(_ sender: UISlider) {
        if let slider = sender.accessibilityLabel {
            let flavorValue = Int(roundf(sender.value))
            switch slider {
            case "sweetness":
                sweetnessLabel.text = "Dulzura: \(flavorValue)"
            case "acidity":
                acidityLabel.text = "Acidez: \(flavorValue)"
            case "flowery":
                floweryLabel.text = "Florar: \(flavorValue)"
            case "spicy":
                spicyLabel.text = "Especiado: \(flavorValue)"
            case "salty":
                saltyLabel.text = "Salado: \(flavorValue)"
            case "berries":
                berriesLabel.text = "Frutos Rojos: \(flavorValue)"
            case "chocolate":
                chocolateLabel.text = "Chocolate: \(flavorValue)"
            case "candy":
                candyLabel.text = "Caramelo: \(flavorValue)"
            case "body":
                bodyLabel.text = "Cuerpo: \(flavorValue)"
            case "cleaning":
                cleaningLabel.text = "Limpieza: \(flavorValue)"
            default:
                break
            }
        }
    }
    
    @IBAction func saveCircleFlavor() {
        self.startLoading()
        CheckinManager.createCircleFlavor(
            checkinId: checkin.id,
            circleFlavor:  getCircleFlavor(),
            onSuccess: {(checkin: Checkin) -> () in
                self.stopLoading()
                self.circleFlavorDelegate?.updateCircleFlavor(checkin: checkin, circleFlavor: self.circleFlavor)
                _ = self.navigationController?.popViewController(animated: true)
            },
            onError: {(error: String) -> () in
                self.stopLoading()
                print(error)
        })
    }
    
    func getCircleFlavor() -> CircleFlavor {
        self.circleFlavor = CircleFlavor(sweetness: Int(roundf(sweetnessSlider.value)),
                                         acidity: Int(roundf(aciditySlider.value)),
                                         flowery: Int(roundf(flowerySlider.value)),
                                         spicy: Int(roundf(spicySlider.value)),
                                         salty: Int(roundf(saltySlider.value)),
                                         berries: Int(roundf(berriesSlider.value)),
                                         chocolate: Int(roundf(chocolateSlider.value)),
                                         candy: Int(roundf(candySlider.value)),
                                         body: Int(roundf(bodySlider.value)),
                                         cleaning: Int(roundf(cleaningSlider.value)))
        return circleFlavor
    }
    
    func startLoading(){
        activityIndicator.center = self.view.center;
        activityIndicator.hidesWhenStopped = true;
        activityIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray;
        self.view.addSubview(activityIndicator);
        self.view.bringSubview(toFront: activityIndicator)
        activityIndicator.startAnimating();
        UIApplication.shared.beginIgnoringInteractionEvents();
        
    }
    
    func stopLoading(){
        activityIndicator.stopAnimating();
        UIApplication.shared.endIgnoringInteractionEvents();
    }
}

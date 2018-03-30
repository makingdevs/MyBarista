//
//  CheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Cosmos
import FBSDKShareKit
import FBSDKCoreKit

class CheckinViewController: UIViewController, CheckinDelegate, FBSDKSharingDelegate {
    func sharer(_ sharer: FBSDKSharing!, didCompleteWithResults results: [AnyHashable : Any]!) {

    }
    
    func sharer(_ sharer: FBSDKSharing!, didFailWithError error: Error!) {
        self.present(self.showErrorAlert(message:"No es posible compartir tu publicación"), animated: true)
    }
    
    func sharerDidCancel(_ sharer: FBSDKSharing!) {

    }
    
    
    var checkin:Checkin!
    
    @IBOutlet weak var methodLabel: UILabel!
    @IBOutlet weak var stateLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var checkinPhotoView: UIImageView!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var ratingView: CosmosView!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
      let backItem = UIBarButtonItem()
      backItem.title = ""
      navigationItem.backBarButtonItem = backItem
        showCheckinDetail()
        initRatingView()
    }
    
    func showCheckinDetail () {
        methodLabel.text = checkin.method
        stateLabel.text = checkin.state
        priceLabel!.text = "$ \(checkin.price ?? "")"
        noteLabel.text = checkin.note ?? " "
      
        if noteLabel.text == ""{//this code is to avoid issue in empty stack view
          noteLabel.text = " "
        }
      
        if checkin.s3Asset != nil {
            checkinPhotoView.loadURL(url: (checkin.s3Asset?.urlFile)!)
        }
    }
    
    /* Protocol function that updates check-in after edition */
    func updateCheckinDetail(currentCheckin: Checkin) {
        self.checkin = currentCheckin
        showCheckinDetail()
    }
    
    func initRatingView() {
        if let rating = checkin.rating {
            ratingView.loadRating(rating: rating)
        }
        
        ratingView.didFinishTouchingCosmos = { rating in
            self.updateRatingInCheckIn(rating: Float(rating))
        }
    }
    
    /* Updates check-in rating */
    func updateRatingInCheckIn(rating: Float) {
        let checkinCommand: CheckinCommand = CheckinCommand(id: checkin.id, rating: rating)
        CheckinManager.saveRating(
            checkinCommand: checkinCommand,
            onSuccess: { (checkin: Checkin) -> () in
                self.checkin = checkin
        },
            onError: { (error: String) -> () in
                print(error)
        })
    }
    
    @IBAction func addNote(_ sender: UIButton) {
        let alert = UIAlertController(title: "Describe tu experiencia", message: nil, preferredStyle: .alert)
        alert.addTextField()
        alert.textFields?[0].text = self.checkin.note
        let okAction = UIAlertAction(title: "Guardar", style: .default) { (action) in
            let note = alert.textFields?[0].text
            if let note = note {
                self.updateNoteInCheckIn(note: note)
            }
        }
        alert.addAction(okAction)
        present(alert, animated: true)
    }
    
    /* Updates check-in note */
    func updateNoteInCheckIn(note: String) {
        let checkinCommand: CheckinCommand = CheckinCommand(id: checkin.id, note: note)
        CheckinManager.saveNote(
            checkinCommand: checkinCommand,
            onSuccess: { (checkin: Checkin) -> () in
                self.checkin = checkin
                self.noteLabel.text = checkin.note
        },
            onError: { (error: String) -> () in
                print(error)
        })
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "performUpdate" {
            let createCheckinController = segue.destination as! CreateCheckinViewController
            createCheckinController.checkin = self.checkin
            createCheckinController.checkInAction = "UPDATE"
            createCheckinController.checkinDelegate = self
        } else if segue.identifier == "performCircleFlavor" {
            let circleFlavorController = segue.destination as! CircleFlavourViewController
            circleFlavorController.checkin = self.checkin
        }
    }
    @IBAction func shareWithFacebook(_ sender: Any) {
        let image = checkinPhotoView.image
        let sharePhoto = FBSDKSharePhoto()
        sharePhoto.image = image;
        sharePhoto.isUserGenerated = true;
        let content = FBSDKSharePhotoContent()
        content.photos = [sharePhoto]
        _ = FBSDKShareDialog.show(from: self, with: content, delegate: self)
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Ocurrió un error", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
    }
}

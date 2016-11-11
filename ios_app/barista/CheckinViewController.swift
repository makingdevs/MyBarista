//
//  CheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Cosmos

class CheckinViewController: UIViewController {
    
    var checkin:Checkin!
    
    @IBOutlet weak var methodLabel: UILabel!
    @IBOutlet weak var stateLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var checkinPhotoView: UIImageView!
    @IBOutlet weak var venueLabel: UILabel!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var ratingLabel: UILabel!
    @IBOutlet weak var ratingView: CosmosView!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.title = checkin.method
        showCheckinDetail()
        initRatingView()
    }
    
    func showCheckinDetail () {
        checkinPhotoView.loadURL(url: (checkin.s3Asset?.urlFile)!, placeholder: #imageLiteral(resourceName: "coffee_holder"))
        methodLabel.text = checkin.method
        stateLabel.text = checkin.state
        priceLabel!.text = "$ \(checkin.price!)"
        venueLabel.text = checkin.venue
        noteLabel.text = checkin.note
        ratingLabel.text = checkin.rating == 0 ? "0" : String(checkin.rating!)
    }
    
    func initRatingView() {
        ratingView.loadRating(rating: checkin.rating!)
        /* Updates UI as the rating is being changed by touching the view */
        ratingView.didTouchCosmos = { rating in
            self.ratingLabel.text = rating == 0 ? "0" : String(rating)
        }
        /* Sends check-in rating to the server */
        ratingView.didFinishTouchingCosmos = { rating in
            self.checkin.rating = Float(rating)
        }
    }
    
    @IBAction func addNote(_ sender: UIButton) {
        let alert = UIAlertController(title: "Describe tu experiencia", message: nil, preferredStyle: .alert)
        alert.addTextField()
        alert.textFields?[0].text = self.checkin.note
        let okAction = UIAlertAction(title: "Guardar", style: .default) { (action) in
            let note = alert.textFields?[0].text
            self.updateNoteInCheckIn(note: note!)
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
}

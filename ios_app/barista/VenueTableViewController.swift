//
//  VenueTableViewController.swift
//  barista
//
//  Created by Ariana Santillán on 08/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import CoreLocation

class VenueTableViewController: UITableViewController, CLLocationManagerDelegate {
    
    var venues: [Venue] = [Venue]()
    let locationManager = CLLocationManager()

    override func viewDidLoad() {
        super.viewDidLoad()
        initLocationManager()
        // Getting the current location
        let latitude = locationManager.location?.coordinate.latitude;
        let longitude = locationManager.location?.coordinate.longitude;
    }
    
    func initLocationManager() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return venues.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let venueCell = tableView.dequeueReusableCell(withIdentifier: "VenueTableViewCell", for: indexPath) as! VenueTableViewCell
        let venue: Venue = venues[indexPath.row]
        venueCell.venueName.text = venue.name
        venueCell.venueLocation.text = venue.location
        return venueCell
    }
}

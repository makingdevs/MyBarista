//
//  VenueTableViewController.swift
//  barista
//
//  Created by Ariana Santillán on 08/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import CoreLocation

protocol VenueDelegate {
    func updateVenueName(venueSelected: String)
}

class VenueTableViewController: UITableViewController, CLLocationManagerDelegate {
    
    var venues: [Venue] = [Venue]()
    let locationManager = CLLocationManager()
    var venueCommand: VenueCommand!
    var delegate: VenueDelegate!

    override func viewDidLoad() {
        super.viewDidLoad()
        initLocationManager()
        fetchVenues()
    }
    
    func initLocationManager() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }
    
    func fetchVenues() {
        let latitude = locationManager.location?.coordinate.latitude.description;
        let longitude = locationManager.location?.coordinate.longitude.description;
        venueCommand = VenueCommand(latitude: latitude!, longitude: longitude!, query: "")
        FoursquareManager.getVenuesNear(
            venueCommand: venueCommand,
            onSuccess: { (venues: [Venue]) -> () in
                self.venues = venues
                self.tableView.reloadData()
            },
            onError: { (error: String) -> () in
                print(error)
        })
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
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.tableView.deselectRow(at: self.tableView.indexPathForSelectedRow!, animated: false)
        let venue: Venue = venues[indexPath.row]
        self.delegate.updateVenueName(venueSelected: venue.name)
        _ = self.navigationController?.popViewController(animated: true)
    }
}

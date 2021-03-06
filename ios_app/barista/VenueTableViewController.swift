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
    func updateVenueName(venueSelected: Venue)
}

class VenueTableViewController: UITableViewController, CLLocationManagerDelegate, UISearchBarDelegate {
    
    var venues: [Venue] = [Venue]()
    let locationManager = CLLocationManager()
    var venueCommand: VenueCommand!
    var venueDelegate: VenueDelegate?
    var currentLocation : CLLocation?
    
    @IBOutlet weak var venueSearchBar: UISearchBar!
    
    let activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        venueSearchBar.delegate = self
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            switch CLLocationManager.authorizationStatus() {
            case .notDetermined:
                locationManager.requestWhenInUseAuthorization()
                startUpdatingLocation()
            case .authorizedAlways, .authorizedWhenInUse:
                startUpdatingLocation()
            default:
                print("The app is not permitted to use location services")
            }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        view.endEditing(true)
    }
    
    func startUpdatingLocation() {
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.startUpdatingLocation()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let newLocation = manager.location {
            if currentLocation == nil || (currentLocation?.distance(from: newLocation).magnitude)! > 200 {
                currentLocation = newLocation
                fetchVenues(latitude: currentLocation?.coordinate.latitude.description, longitude: currentLocation?.coordinate.longitude.description, query: "")
            }
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error.localizedDescription)
    }
    
    func fetchVenues(latitude: String?, longitude: String?, query: String?){
        guard let latitude = latitude, let longitude = longitude, let query = query else {
            return
        }
        
        venueCommand = VenueCommand(latitude: latitude, longitude: longitude, query: query)
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
        venueCell.venueLocation.text = venue.location.joined(separator: "\n")
        return venueCell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.tableView.deselectRow(at: self.tableView.indexPathForSelectedRow!, animated: false)
        let venue: Venue = self.venues[indexPath.row]
        self.venueDelegate?.updateVenueName(venueSelected: venue)
        _ = self.navigationController?.popViewController(animated: true)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.fetchVenues(latitude: currentLocation?.coordinate.latitude.description, longitude: currentLocation?.coordinate.longitude.description, query: searchBar.text)
        restartSearchBar()
    }
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        venueSearchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.fetchVenues(latitude: currentLocation?.coordinate.latitude.description, longitude: currentLocation?.coordinate.longitude.description, query: "")
        restartSearchBar()
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        self.fetchVenues(latitude: currentLocation?.coordinate.latitude.description, longitude: currentLocation?.coordinate.longitude.description, query: searchBar.text)
    }
    
    func restartSearchBar(){
        venueSearchBar.text = ""
        venueSearchBar.resignFirstResponder()
        venueSearchBar.setShowsCancelButton(false, animated: true)
    }
}

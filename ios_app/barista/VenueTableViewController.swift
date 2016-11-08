//
//  VenueTableViewController.swift
//  barista
//
//  Created by Ariana Santillán on 08/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class VenueTableViewController: UITableViewController {
    
    var venues: [Venue] = [Venue]()

    override func viewDidLoad() {
        super.viewDidLoad()
        loadSampleVenues()
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
    
    func loadSampleVenues() {
        let venue = Venue(id: 0, name: "Espressarte", location: "México")
        venues += [venue, venue, venue]
    }
}

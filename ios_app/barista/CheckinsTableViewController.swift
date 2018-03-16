//
//  CheckinsViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CheckinsTableViewController: UITableViewController {
    
    var checkins:[Checkin] = [Checkin]()
    let userPreferences = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if userPreferences.object(forKey: "currentUser") != nil {
            retriveCheckinsForManager(username: userPreferences.string(forKey: "currentUser")!)
        }
    }
    
    fileprivate func getDaysSinceNow(from someDate: Date) -> String {
        let yesterday = Date(timeInterval: -86400, since: someDate)
        let tomorrow = Date(timeInterval: 86400, since: Date())
        let diff = tomorrow.interval(ofComponent: .day, fromDate: yesterday)
        
        if diff < 1{
            return "Hace unas hroas"
        }else if diff == 1 {
            return "Hace un día"
        }
        return "Hace \(diff) dias"
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //TODO: Improve this piece of code
        let viewCell = tableView.dequeueReusableCell(withIdentifier: "CheckinsTableViewCell", for: indexPath) as! CheckinsTableViewCell
        let checkin = checkins[indexPath.row]
        viewCell.methodLabel?.text = checkin.method
        viewCell.coffeeOriginLabel?.text = checkin.state
        viewCell.ratingView.loadRating(rating: checkin.rating!)
        viewCell.createdAtLabel.text = self.getDaysSinceNow(from: checkin.createdAt!)
        if (checkin.s3Asset != nil) {
            viewCell.coffeeImageView.loadURL(url: (checkin.s3Asset?.urlFile)!)
        }
        return viewCell
    }
    
    
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return checkins.count
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowCheckin" {
            let checkinViewController:CheckinViewController = segue.destination as! CheckinViewController
            checkinViewController.checkin = checkins[((tableView.indexPathForSelectedRow as NSIndexPath?)?.row)!]
        }
    }
    
    func retriveCheckinsForManager(username: String){
        CheckinManager.findAllCheckinsByUser(
            username,
            onSuccess: { (checkins:[Checkin]) -> () in
                self.checkins = checkins
                self.tableView.reloadData()
            },
            onError:{ (error:String) -> () in
                print(error)
        })
    }
}

//TODO move it to a new file
extension Date {
    
    func interval(ofComponent comp: Calendar.Component, fromDate date: Date) -> Int {
        
        let currentCalendar = Calendar.current
        
        guard let start = currentCalendar.ordinality(of: comp, in: .era, for: date) else { return 0 }
        guard let end = currentCalendar.ordinality(of: comp, in: .era, for: self) else { return 0 }
        
        return end - start
    }
}

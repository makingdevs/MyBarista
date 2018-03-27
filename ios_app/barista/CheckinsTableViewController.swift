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
    
    fileprivate func getDaysUntilNow(from someDate: Date) -> String {
        let components = Calendar.current.dateComponents([.month, .day, .hour], from: someDate, to: Date())
        var timeFromNow = "Hace "
        if let month = components.month, month > 0 {
            if month == 1{
                timeFromNow = "\(timeFromNow) un mes"
            }else{
                timeFromNow = "\(timeFromNow) \(month) meses"
            }
        }
        if let day = components.day, day > 0{
            if day == 1{
                timeFromNow = " \(timeFromNow) un día"
            }else{
                timeFromNow = " \(timeFromNow) \(day) días"
            }
        }
        if let hour = components.hour, hour > 0{
            if hour == 1{
                timeFromNow = " \(timeFromNow) una hora"
            }else{
                timeFromNow = " \(timeFromNow) \(hour) horas"
            }
        }
        if components.month ?? 0 <= 0 && components.day ?? 0 <= 0 && components.hour ?? 0 <= 0{
            timeFromNow = "Hace poco"
        }
        return timeFromNow
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //TODO: Improve this piece of code
        let viewCell = tableView.dequeueReusableCell(withIdentifier: "CheckinsTableViewCell", for: indexPath) as! CheckinsTableViewCell
        let checkin = checkins[indexPath.row]
        viewCell.methodLabel?.text = checkin.method
        viewCell.coffeeOriginLabel?.text = checkin.state
        if let rating = checkin.rating {
            viewCell.ratingView.loadRating(rating: rating)
        }
        if let createdAt = checkin.createdAt{
            viewCell.createdAtLabel.text = self.getDaysUntilNow(from: createdAt)
        }
        if let s3Asset = checkin.s3Asset, let urlFile = s3Asset.urlFile {
            viewCell.coffeeImageView.loadURL(url: urlFile)
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

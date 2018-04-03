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
        
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
        
        if userPreferences.object(forKey: "currentUser") != nil {
            retriveCheckinsForManager(username: userPreferences.string(forKey: "currentUser")!)
        }
    }
    
    fileprivate func getDaysUntilNow(from someDate: Date) -> String {
        let components = Calendar.current.dateComponents([.month, .day, .hour], from: someDate, to: Date())
        var timeFromNow = ""
        if let month = components.month, month > 0 {
            if month == 1{
                timeFromNow = "Hace \(timeFromNow) un mes"
            }else{
                timeFromNow = "Hace \(timeFromNow) \(month) meses"
            }
        } else if let day = components.day, day > 0{
            if day == 1{
                timeFromNow = "Hace \(timeFromNow) un día"
            }else{
                timeFromNow = "Hace \(timeFromNow) \(day) días"
            }
        }else if let hour = components.hour, hour > 0{
            if hour == 1{
                timeFromNow = "Hace \(timeFromNow) una hora"
            }else{
                timeFromNow = "Hace \(timeFromNow) \(hour) horas"
            }
        } else if components.month ?? 0 <= 0 && components.day ?? 0 <= 0 && components.hour ?? 0 <= 0{
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
        }else{
            viewCell.coffeeImageView.image = #imageLiteral(resourceName: "coffee_holder")
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
            
            let backItem = UIBarButtonItem()
            backItem.title = ""
            navigationItem.backBarButtonItem = backItem
        }
    }
    
    func retriveCheckinsForManager(username: String){
        CheckinManager.findAllCheckinsByUser(
            username,
            onSuccess: { (checkins:[Checkin]) -> () in
                self.checkins = checkins
                if checkins.count == 0 {
                    self.tabBarController?.selectedIndex = 1
                }else{
                    self.tableView.reloadData()
                }
            },
            onError:{ (error:String) -> () in
                print("retriveCheckinsForManager \(error)")
        })
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
}

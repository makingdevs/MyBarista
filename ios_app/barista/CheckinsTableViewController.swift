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
    var user: User!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        retriveCheckinsForManager()
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //TODO: Improve this piece of code
        let viewCell = tableView.dequeueReusableCell(withIdentifier: "CheckinsTableViewCell", for: indexPath) as! CheckinsTableViewCell
        let checkin = checkins[indexPath.row]
        viewCell.methodLabel?.text = checkin.method
        viewCell.coffeeOriginLabel?.text = checkin.state
        viewCell.ratingView.loadRating(rating: checkin.rating!)
        viewCell.coffeeImageView.loadURL(url: checkin.urlPhoto!, placeholder: #imageLiteral(resourceName: "coffee_holder"))
        return viewCell
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return checkins.count
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let checkinViewController:CheckinViewController = segue.destination as! CheckinViewController
        checkinViewController.checkin = checkins[((tableView.indexPathForSelectedRow as NSIndexPath?)?.row)!]
    }
    
    func retriveCheckinsForManager(){
        CheckinManager.findAllCheckinsByUser(user.username,
          onSuccess: { (checkins:[Checkin]) -> () in
                self.checkins = checkins
                self.tableView.reloadData()
          },
          onError:{ (error:String) -> () in
            print("Errorsss" + error)
          })
    }
}

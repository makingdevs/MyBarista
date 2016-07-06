//
//  CheckinsViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CheckinsTableViewController: UITableViewController {
    
    var checkins:[Checkin] = []

    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        retriveCheckinsForManager()
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let viewCell = tableView.dequeueReusableCellWithIdentifier("checkinCell", forIndexPath: indexPath)
        viewCell.textLabel?.text = checkins[indexPath.row].method
        viewCell.detailTextLabel?.text = checkins[indexPath.row].origin
        
        return viewCell
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return checkins.count
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let checkinViewController:CheckinViewController = segue.destinationViewController as! CheckinViewController
        checkinViewController.checkin = checkins[(tableView.indexPathForSelectedRow?.row)!]
    }
    
    func retriveCheckinsForManager(){
        CheckinManager.findAllCheckinsByUser("cggg88jorge",
          onSuccess: { (checkins:[Checkin]) -> () in
                self.checkins = checkins
                self.tableView.reloadData()
          },
          onError:{ (error:String) -> () in
            print("Errorsss" + error)
          })
    }
}
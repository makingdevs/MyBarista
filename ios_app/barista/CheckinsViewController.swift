//
//  CheckinsViewController.swift
//  barista
//
//  Created by MakingDevs on 7/6/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CheckinsViewController: UITableViewController {
    
    var checkins:[Checkin] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        let checkin1 = Checkin(id:1)
        let checkin2 = Checkin(id:2)
        let checkin3 = Checkin(id:3)
        checkins.append(checkin1)
        checkins.append(checkin2)
        checkins.append(checkin3)
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let viewCell = tableView.dequeueReusableCellWithIdentifier("checkinCell", forIndexPath: indexPath)
        viewCell.textLabel?.text = "\(checkins[indexPath.row].id)"
        return viewCell
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return checkins.count
    }
}
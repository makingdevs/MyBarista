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
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        retriveCheckinsForManager()
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let viewCell = tableView.dequeueReusableCell(withIdentifier: "checkinCell", for: indexPath)
        viewCell.textLabel?.text = checkins[(indexPath as NSIndexPath).row].method
        viewCell.detailTextLabel?.text = checkins[(indexPath as NSIndexPath).row].origin
        
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

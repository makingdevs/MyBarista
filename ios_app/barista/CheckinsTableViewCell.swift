//
//  CheckinsTableViewCell.swift
//  barista
//
//  Created by Ariana Santillán on 20/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Cosmos

class CheckinsTableViewCell: UITableViewCell {
    
    // MARK: Properties
    @IBOutlet weak var coffeeImageView: UIImageView!
    @IBOutlet weak var methodLabel: UILabel!
    @IBOutlet weak var coffeeOriginLabel: UILabel!
    @IBOutlet weak var createdAtLabel: UILabel!
    @IBOutlet weak var ratingView: CosmosView!
    

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

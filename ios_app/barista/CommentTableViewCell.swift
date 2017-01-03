//
//  CommentTableViewCell.swift
//  barista
//
//  Created by MakingDevs on 22/12/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CommentTableViewCell: UITableViewCell {
    
    @IBOutlet weak var author: UILabel!
    @IBOutlet weak var createdAt: UILabel!
    @IBOutlet weak var bodyComment: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}

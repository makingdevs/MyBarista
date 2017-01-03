//
//  CommentTableViewController.swift
//  barista
//
//  Created by MakingDevs on 23/12/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit

class CommentTableViewController: UITableViewController {
    
    @IBOutlet weak var commentField: UITextField!
    @IBOutlet weak var saveCommentButton: UIButton!
    
    var checkin: Checkin!
    var comments: [Comment] = [Comment]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        saveCommentButton.isEnabled = false
        commentField.addTarget(self, action: #selector(textFieldDidChange(textField:)), for: UIControlEvents.editingChanged)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        fetchCheckInComments()
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return comments.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let commentCell = tableView.dequeueReusableCell(withIdentifier: "CommentTableViewCell", for: indexPath) as! CommentTableViewCell
        let comment: Comment = comments[indexPath.row]
        commentCell.author.text = comment.author
        commentCell.bodyComment.text = comment.body
        commentCell.createdAt.text = String(describing: comment.created_at!)
        return commentCell
    }
    
    func fetchCheckInComments() {
        CommentManager.sharedInstance.fetchComments(
            checkinId: checkin.id,
            onSuccess: {(comments: [Comment]) -> () in
                self.comments = comments
                self.tableView.reloadData()
        },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    @IBAction func getComment(_ sender: UIButton) {
        CommentManager.sharedInstance.saveComment(
            commentCommand: self.initCommentCommand(),
            onSuccess: {(comment: Comment) -> () in
                self.fetchCheckInComments()
                self.commentField.text = ""
        },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    /* Validates non-empty comments */
    func textFieldDidChange(textField: UITextField) {
        if textField.text == "" {
            saveCommentButton.isEnabled = false
        } else {
            saveCommentButton.isEnabled = true
        }
    }
    
    func initCommentCommand() -> CommentCommand {
        let comment: String = commentField.text!
        let commentCommand: CommentCommand = CommentCommand(author: checkin.author!,
                                                            body: comment,
                                                            checkinId: String(checkin.id),
                                                            createdAt: Date())
        return commentCommand
    }
}

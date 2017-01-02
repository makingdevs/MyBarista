//
//  CommentManager.swift
//  barista
//
//  Created by MakingDevs on 09/12/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class CommentManager {
    
    static let sharedInstance = CommentManager()
    
    func fetchComments(checkinId: Int, onSuccess: @escaping (_ comments: [Comment]) -> (), onError:@escaping (_ error: String) -> ()) {
        
        let commentsPath: String = "\(Constants.urlBase)/checkins/\(checkinId)/comments"
        
        Alamofire.request(commentsPath)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    var comments = [Comment]()
                    if let value = response.result.value {
                        let json = JSON(value)
                        for(_, json) in
                            json {
                                comments.append(CommentManager.sharedInstance.parseCommentJSON(json: json))
                        }
                        onSuccess(comments)
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
    
    func saveComment(commentCommand: CommentCommand, onSuccess: @escaping (_ comment: Comment) -> (), onError: @escaping (_ error: String) -> ()) {
        
        let commentPath: String = "\(Constants.urlBase)/comments/"
        let commentParams = ["username": commentCommand.author!,
                             "body": commentCommand.body!,
                             "checkin_id": commentCommand.checkinId!,
                             "created_at": commentCommand.createdAt!] as [String : Any]
        
        Alamofire.request(commentPath, method: .post, parameters: commentParams)
            .validate(statusCode: 200..<202)
            .responseJSON {
                response in
                switch response.result {
                case .success:
                    if let value = response.result.value {
                        let json = JSON(value)
                        onSuccess(CommentManager.sharedInstance.parseCommentJSON(json: json))
                    }
                case .failure(let error):
                    onError(error.localizedDescription)
                }
        }
    }
    
    func parseCommentJSON(json: JSON) -> Comment {
        let commentAuthor = json["user"]["username"].stringValue
        let commentBody = json["body"].stringValue
        let commentDate = json["created_at"].timeValue
        let comment = Comment(author: commentAuthor, body: commentBody, created_at: commentDate as! Date)
        return comment
    }
}

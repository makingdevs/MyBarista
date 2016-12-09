//
//  CommentManager.swift
//  barista
//
//  Created by MakingDevs on 09/12/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import Foundation
import Alamofire

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
                    print(response)
                case .failure(let error):
                    print(error.localizedDescription)
                }
        }
    }
}

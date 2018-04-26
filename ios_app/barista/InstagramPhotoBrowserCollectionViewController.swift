//
//  InstagramPhotoBrowserCollectionViewController.swift
//  barista
//
//  Created by marco antonio reyes  on 25/04/18.
//  Copyright © 2018 MakingDevs. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

private let reuseIdentifier = "Cell"

class InstagramPhotoBrowserCollectionViewController: UICollectionViewController {

    var urlImages: [String]?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Register cell classes
        self.collectionView!.register(UICollectionViewCell.self, forCellWithReuseIdentifier: reuseIdentifier)

        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if isLoged(){
            refreshImages()
        }else{
            self.performSegue(withIdentifier: "performWebView", sender: self)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

    // MARK: UICollectionViewDataSource

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 0
    }


    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of items
        return 0
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath)
    
        // Configure the cell
    
        return cell
    }

    func isLoged() -> Bool{
        let userPreferences = UserDefaults.standard
        if (userPreferences.value(forKey: "instagram_token") != nil) {
            return true;
        }
        return false
    }
    
    func refreshImages(){
        
        Alamofire.request(Router.popularPhotos).responseJSON{response in
            let result = response.result
            print(response)
            switch result {
            case .success(let jsonObject):
                let json = JSON(jsonObject)
                
                if (json["meta"]["code"].intValue  == 200) {
//                    DispatchQueue.global(DispatchQueue.GlobalQueuePriority.high).async() {
//                        if let urlString = json["pagination"]["next_url"].URL {
//                            self.nextURLRequest = NSURLRequest(URL: urlString)
//                        } else {
//                            self.nextURLRequest = nil
//                        }
//                        let photoInfos = json["data"].arrayValue
//
//                            .filter {
//                                $0["type"].stringValue == "image"
//                            }.map({
//                                PhotoInfo(sourceImageURL: $0["images"]["standard_resolution"]["url"].URL!)
//                            })
//
//                        let lastItem = self.photos.count
//                        self.photos.appendContentsOf(photoInfos)
//
//                        let indexPaths = (lastItem..<self.photos.count).map { NSIndexPath(forItem: $0, inSection: 0) }
//
//                        dispatch_async(dispatch_get_main_queue()) {
//                            self.collectionView!.insertItemsAtIndexPaths(indexPaths)
//                        }
//
//                    }
                    
                }
            case .failure: break
            }
        }
    }
    // MARK: UICollectionViewDelegate

    /*
    // Uncomment this method to specify if the specified item should be highlighted during tracking
    override func collectionView(_ collectionView: UICollectionView, shouldHighlightItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment this method to specify if the specified item should be selected
    override func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment these methods to specify if an action menu should be displayed for the specified item, and react to actions performed on the item
    override func collectionView(_ collectionView: UICollectionView, shouldShowMenuForItemAt indexPath: IndexPath) -> Bool {
        return false
    }

    override func collectionView(_ collectionView: UICollectionView, canPerformAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) -> Bool {
        return false
    }

    override func collectionView(_ collectionView: UICollectionView, performAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) {
    
    }
    */

}

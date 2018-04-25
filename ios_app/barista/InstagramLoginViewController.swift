//
//  InstagramLoginViewController.swift
//  barista
//
//  Created by marco antonio reyes  on 24/04/18.
//  Copyright © 2018 MakingDevs. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class InstagramLoginViewController: UIViewController {

    @IBOutlet weak var webView: UIWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        webView.delegate = self
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        URLCache.shared.removeAllCachedResponses()
        if let cookies = HTTPCookieStorage.shared.cookies {
            print(cookies)
            for cookie in cookies {
                HTTPCookieStorage.shared.deleteCookie(cookie)
            }
        }
        
        do {
        let url = try Router.requestOauthCode.asURLRequest().url!
        print(url.description)
        let request = URLRequest(url:url , cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10.0)
            self.webView.loadRequest(request)
            print("showInstagramImages")
        } catch {
            print("Error obteniendo la url del token")
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
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

extension InstagramLoginViewController: UIWebViewDelegate{
    func webView(_ webView: UIWebView, shouldStartLoadWith request:URLRequest, navigationType: UIWebViewNavigationType) -> Bool{
        print("shouldStartLoadWith")
        return checkRequestForCallbackURL(request: request)
    }
    
    func checkRequestForCallbackURL(request: URLRequest) -> Bool {
        print("checkRequestForCallbackURL")
        
        let redirectURIComponents = NSURLComponents(string: Router.redirectURI)!
        let components = NSURLComponents(string:(request.url?.absoluteString)! as String)!
        if components.host == redirectURIComponents.host {
            if let code = (components.queryItems?.filter { $0.name == "code" })?.first?.value {
                debugPrint(code)
                requestAccessToken(code: code)
                return false
            }
        }
        return true
    }
    
    func requestAccessToken(code: String) {
        let requestData = Router.requestAccessTokenURLStringAndParms(code: code)
        print("requestAccessToken")
        print(requestData.params)

        Alamofire.request(requestData.urlString, method: .post, parameters: requestData.params, encoding: URLEncoding.default, headers: ["Accept": "application/json"])
            .responseJSON { response in
                print(response)
                let result = response.result
                print(result)
                switch result {
                case .success(let jsonObject):
                    //debugPrint(jsonObject)
                    let json = JSON(jsonObject)
                    
                    if let accessToken = json["access_token"].string, let _ = json["user"]["id"].string {
                        print("token... \(accessToken)")
                        self.setInstagramToken(token: accessToken)
                        self.dismiss(animated: true, completion: nil)
                    }
                case .failure:
                    break;
                }
        }
    }
    
    func setInstagramToken(token: String) {
        let userPreferences = UserDefaults.standard
        userPreferences.set(token, forKey: "instagram_token")
        userPreferences.synchronize()
    }
    func handleAuth(authToken: String) {
        print("Instagram authentication token ==", authToken)
    }
}

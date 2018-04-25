//
//  CreateCheckinViewController.swift
//  barista
//
//  Created by MakingDevs on 28/10/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import ImagePicker


protocol CheckinDelegate {
    func updateCheckinDetail(currentCheckin: Checkin)
}

class CreateCheckinViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UINavigationControllerDelegate, VenueDelegate, ImagePickerDelegate, UITextFieldDelegate{
    
    @IBOutlet weak var checkinPhoto: UIImageView!
    @IBOutlet weak var methodField: UITextField!
    @IBOutlet weak var stateField: UITextField!
    @IBOutlet weak var originField: UITextField!
    @IBOutlet weak var priceField: UITextField!
    @IBOutlet weak var venueLabel: UIButton!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var constraintContentHeight: NSLayoutConstraint!
    @IBOutlet weak var scrollView: UIScrollView!
    
    @IBOutlet weak var webView: UIWebView!
    let methodPickerView = UIPickerView()
    let statePickerView = UIPickerView()
    let activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView();
    
    var methodList = ["Expresso", "Americano", "Goteo", "Prensa", "Sifón", "Otro"]
    var stateList = ["Veracruz", "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Otro"]
    
    let userPreferences = UserDefaults.standard
    var uploadCommand: UploadCommand!
    var checkinCommand: CheckinCommand!
    var checkinDelegate: CheckinDelegate?
    var checkInAction: String = "CREATE"
    var checkin: Checkin?
    var method: String!
    var state: String!
    var origin: String!
    var price: String!
    var image: UIImage?
    var venue: String?
    
    var activeField: UITextField?
    var lastOffset: CGPoint!
    var keyboardHeight: CGFloat!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        methodField.delegate = self
        stateField.delegate = self
        originField.delegate = self
        priceField.delegate = self
        
        
        methodField.underlined()
        stateField.underlined()
        originField.underlined()
        priceField.underlined()
        
        self.hideKeyboardWhenTappedAround()
        initPickerViews()
        method = methodList[0]
        state = stateList[0]
        if checkin != nil {
            setCurrentCheckIn()
        }
        saveButton.bordered()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        webView.isHidden = true
        
        
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.removeObserver(self)
    }
    
    func cleanView(){
        methodField.text = methodList[0]
        stateField.text = stateList[0]
        originField.text = ""
        priceField.text = ""
        venueLabel.setTitle("Agrega un lugar", for: .normal)
        checkin?.method = nil
        checkin?.state = nil
        checkin?.origin = nil
        checkin?.price = nil
        checkin?.venue = nil
        checkin?.s3Asset = nil
        checkinPhoto.image =  UIImage(named: "coffee_holder")
    }
    
    func setCurrentCheckIn() {
        methodField.text = checkin?.method
        stateField.text = checkin?.state
        originField.text = checkin?.origin
        priceField.text = checkin?.price
        if let venue = checkin?.venue, venue != "" {
            venueLabel.setTitle( checkin?.venue, for: .normal)
        }else{
            venueLabel.setTitle( "Agrega un lugar", for: .normal)
        }
      
        if let venueId = checkin?.venueId {
            self.venue = "\(venueId)"
        }
      
        if let s3Asset = checkin?.s3Asset, let urlFile = s3Asset.urlFile {
            checkinPhoto.loadURL(url: urlFile)
        }
    }
    
    func updateVenueName(venueSelected: Venue) {
        self.venue = venueSelected.id
        self.venueLabel.setTitle(venueSelected.name, for: .normal)
    }
    
    @IBAction func saveCheckin(_ sender: Any) {
        getCheckInForm(asset: nil)
        uploadCommand = UploadCommand(image: image)
        if uploadCommand.validateCommand() {
            self.startLoading()
            S3AssetManager.uploadCheckinPhoto(
                uploadCommand: uploadCommand,
                onPhotoSuccess: { (photoCheckin: PhotoCheckin?) -> () in
                    self.stopLoading()
                    if let checkinId = photoCheckin?.id {
                        self.saveCheckin(asset: checkinId)
                    }else{
                        self.present(self.showErrorAlert(message: "Error al guardar la imagen"), animated: true)
                    }
            },
                onPhotoError: { (error: String) -> () in
                    self.stopLoading()
                    print("Photo: \(error.description)")
            })
        } else {
            if checkin?.s3Asset != nil {
                saveCheckin(asset: checkin?.s3Asset?.id)
            } else {
                saveCheckin(asset: nil)
            }
        }
    }
    @IBAction func cancelEdittingCheckin(_ sender: UIBarButtonItem) {
        
        if checkInAction == "CREATE" {
            _ = self.tabBarController?.selectedIndex = 0
        } else {
            _ = self.navigationController?.popViewController(animated: true)
        }
    }
    
    func saveCheckin (asset: Int?) {
        getCheckInForm(asset: asset)
        self.startLoading()
        switch checkInAction {
        case "CREATE":
            CheckinManager.create(
                checkinCommand: checkinCommand,
                onSuccess: { (checkin: Checkin) -> () in
                    self.stopLoading()
                    self.cleanView()
                    _ = self.tabBarController?.selectedIndex = 0
                },
                onError: { (error: String) -> () in
                    self.stopLoading()
                    self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        case "UPDATE":
            guard let checkinId = checkin?.id else{
                return
            }
            
            CheckinManager.update(
                checkinId: checkinId,
                checkinCommand: checkinCommand,
                onSuccess: { (checkin: Checkin) -> () in
                    self.stopLoading()
                    self.checkinDelegate?.updateCheckinDetail(currentCheckin: checkin)
                    _ = self.navigationController?.popViewController(animated: true)
                },
                onError: { (error: String) -> () in
                    self.stopLoading()
                    self.present(self.showErrorAlert(message: error.description), animated: true)
            })
        default:
            self.stopLoading()
            break
        }
    }
    
    func getCheckInForm(asset: Int?) {
        price = priceField.text!
        origin = originField.text!
        var asignedDate = Date()
        if checkInAction == "CREATE", let createdAt = checkin?.createdAt{
            asignedDate = createdAt
        }
        if let currentUser = userPreferences.string(forKey: "currentUser") {
            checkinCommand = CheckinCommand(username: currentUser,
                                        method: method,
                                        state: state,
                                        origin: origin,
                                        price: price,
                                        idS3Asset: asset,
                                        idVenueFoursquare: venue,
                                        created_at: asignedDate)
        }
    }
    
    func showErrorAlert(message: String) -> UIAlertController {
        let alert = UIAlertController(title: "Algo salió mal", message: message, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Aceptar", style: .default) { (action) in }
        alert.addAction(okAction)
        return alert
    }
    
    func slectImageHandler(alert: UIAlertAction!) {
        switch alert.title {
            case "Instagram":
                self.performSegue(withIdentifier: "performWebView", sender: self)
                //showInstagramImages()
                break
            case "Rollo de camara":
                break
            case "Tomar foto":
                break
            default: break
        }
    }
    
    func showInstagramImages(){
        do {
            self.webView.isHidden = false
            let request = URLRequest(url: try Router.requestOauthCode.asURLRequest().url!, cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10.0)
            self.webView.loadRequest(request)
            print("showInstagramImages")
        } catch {
            print("Error obteniendo la url del token")
        }
    }
    
    
    
    func showImageOptions(){
        let alert = UIAlertController(title: "Carga Imagen", message: "¿Desde donde quieres cargar tu imagen?", preferredStyle: UIAlertControllerStyle.actionSheet)
        alert.addAction(UIAlertAction(title: "Rollo de camara", style: UIAlertActionStyle.default, handler: slectImageHandler))
        alert.addAction(UIAlertAction(title: "Tomar foto", style: UIAlertActionStyle.default, handler: slectImageHandler))
        alert.addAction(UIAlertAction(title: "Instagram", style: UIAlertActionStyle.default, handler: slectImageHandler))
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.cancel, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func selectPicture(_ sender: Any?) {
        
        
//      var config = Configuration()
//      config.doneButtonTitle = "OK"
//      config.noImagesTitle = "Lo sentimos, no hay imagenes disponibles"
//      config.recordLocation = false
//      config.allowVideoSelection = false
//
//
//      let imagePicker = ImagePickerController(configuration: config)
//      imagePicker.imageLimit = 1
//      imagePicker.delegate = self
//
//      present(imagePicker, animated: true, completion: nil)
        showImageOptions()
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == methodPickerView {
            return methodList.count
        } else {
            return stateList.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == methodPickerView {
            return methodList[row]
        } else {
            return stateList[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == methodPickerView {
            method = methodList[row]
            methodField.text = methodList[row]
        } else {
            state = stateList[row]
            stateField.text = stateList[row]
        }
        self.view.endEditing(true)
    }
    
    func initPickerViews() {
        methodPickerView.delegate = self
        statePickerView.delegate = self
        methodPickerView.backgroundColor = UIColor.white
        statePickerView.backgroundColor = UIColor.white
        methodField.inputView = methodPickerView
        stateField.inputView = statePickerView
        methodField.text = methodList[0]
        stateField.text = stateList[0]
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "performSelectVenue" {
            let venueTableViewController = segue.destination as! VenueTableViewController
            venueTableViewController.venueDelegate = self
            let backItem = UIBarButtonItem()
            backItem.title = ""
            navigationItem.backBarButtonItem = backItem
        }
    }
  
  // MARK: - ImagePickerDelegate
  func cancelButtonDidPress(_ imagePicker: ImagePickerController) {
    imagePicker.dismiss(animated: true, completion: nil)
  }
  
  func wrapperDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
    guard images.count > 0 else { return }
    self.image = images[0]
    checkinPhoto.image = self.image

    imagePicker.dismiss(animated: true, completion: nil)
  }
  
  func doneButtonDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
    guard images.count > 0 else { return }
    self.image = images[0]
    checkinPhoto.image = self.image
    
    imagePicker.dismiss(animated: true, completion: nil)
  }
    
    func startLoading(){
        activityIndicator.center = self.view.center;
        activityIndicator.hidesWhenStopped = true;
        activityIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray;
        self.view.addSubview(activityIndicator);
        self.view.bringSubview(toFront: activityIndicator)
        activityIndicator.startAnimating();
        UIApplication.shared.beginIgnoringInteractionEvents();
        
    }
    
    func stopLoading(){
        activityIndicator.stopAnimating();
        UIApplication.shared.endIgnoringInteractionEvents();
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        activeField = textField
        lastOffset = self.scrollView.contentOffset
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        activeField?.resignFirstResponder()
        activeField = nil
        return true
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            
            if keyboardHeight == nil {
                keyboardHeight = keyboardSize.height
                UIView.animate(withDuration: 0.3, animations: {
                    self.constraintContentHeight.constant += self.keyboardHeight + 20
                })
            }
            
            let globalPoint = activeField!.superview?.convert(activeField!.frame.origin, to: nil)
            let collapseSpace = calculateCollapseSpace(globalPoint: globalPoint!, superViewHeigt:self.scrollView.frame.size.height, size: (activeField?.frame.size.height)!)
            if collapseSpace < 0 {
                return
            }

            UIView.animate(withDuration: 0.3, animations: {
                self.scrollView.contentOffset = CGPoint(x: self.lastOffset.x, y: (collapseSpace + self.lastOffset.y + 42))
            })
        }
    }
    
    func calculateCollapseSpace(globalPoint: CGPoint, superViewHeigt: CGFloat, size: CGFloat) -> CGFloat{
//        print("\(superViewHeigt) [ \(globalPoint.y) ] \(size)")
        let distanceToBottom =  superViewHeigt - globalPoint.y - size
//        print("\(keyboardHeight) - \(distanceToBottom)")
        return keyboardHeight - distanceToBottom
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        UIView.animate(withDuration: 0.3) {
            if self.keyboardHeight != nil {
                self.constraintContentHeight.constant -= self.keyboardHeight
            }
            self.scrollView.contentOffset = self.lastOffset
        }
        
        keyboardHeight = nil
    }
}

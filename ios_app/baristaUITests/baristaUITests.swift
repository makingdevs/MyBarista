//
//  baristaUITests.swift
//  baristaUITests
//
//  Created by marco antonio reyes  on 11/04/18.
//  Copyright © 2018 MakingDevs. All rights reserved.
//

import XCTest

class baristaUITests: XCTestCase {
        
    override func setUp() {
        super.setUp()
        
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        let appSnap = XCUIApplication()
        setupSnapshot(appSnap)
        appSnap.launch()

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testExample() {
        // Use recording to get started writing UI tests.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
        
        let app = XCUIApplication()
        
        let usuarioTextField = app.textFields["Usuario"]
        usuarioTextField.tap()
        usuarioTextField.typeText("test2")
        
        let passwordTextfieldSecureTextField = app/*@START_MENU_TOKEN@*/.secureTextFields["password_textfield"]/*[[".secureTextFields[\"Contraseña\"]",".secureTextFields[\"password_textfield\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/
        passwordTextfieldSecureTextField.tap()
        passwordTextfieldSecureTextField.typeText("1234567890")
        snapshot("login_view")
        
        app.buttons["INICIAR SESIÓN"].tap()
        snapshot("checkins_view")
        app.tables.cells.children(matching: .other).element(boundBy: 0).tap()
        
        let tabBarsQuery = app.tabBars
        tabBarsQuery.buttons["Check-In"].tap()
        snapshot("createcheckin_view")
        app.navigationBars["barista.CreateCheckinView"].buttons["Cancelar"].tap()
        tabBarsQuery.buttons["Perfil"].tap()
        snapshot("profile_view")
        app.buttons["EDITAR PERFIL"].tap()
        snapshot("profileedit_view")
        app.navigationBars["barista.EditProfileView"].buttons["Back"].tap()
        app.buttons["CERRAR SESIÓN"].tap()
    }
}

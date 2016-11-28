//
//  ActionControllerTest.swift
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import XCTest

class ActionControllerTest: UITestCaseBase {
    
    private var app: XCUIApplication!
        
    override func setUp() {
        super.setUp()
        
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        XCUIApplication().launch()

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
        app = XCUIApplication()
        app.switches["Action Overlay Switch"].tap()
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
        app = nil
    }
    
    // MARK: - Tests
    
    func testNoAction() {
        openActionController()
        
        // should be no actions
        XCTAssert(app.otherElements["No Actions Warning View"].isHittable);
    }
    
    func testAddAction() {
        // add a few actions
        addActions(actions: [
            Action(id: 1, name: "Action 1"),
            Action(id: 2, name: "Action 2"),
            Action(id: 3, name: "Action 3")
        ])
        
        // open controller
        openActionController()
        
        // check actions
        assertActions(actions: "Action 1", "Action 2", "Action 3")
        
        // close controller
        closeActionController()
        
        // add more actions
        addActions(actions: [
            Action(id: 4, name: "Action 4")
        ])
        
        // open controller
        openActionController()
        
        // check actions
        assertActions(actions: "Action 1", "Action 2", "Action 3", "Action 4")
    }
    
    // MARK: - Helpers
    
    func assertActions(actions: String...) {
        
        let table = app.tables.element;
        var expected = Array<String>()
        expected.append("Actions")
        expected.append(contentsOf: actions)
        checkTable(table, items: expected)
    }
    
    func addActions(actions: Array<Action>) {
        var dict = Dictionary<String, Any>()
        var data = Array<Any>()
        for action in actions {
            data.append([
                "id" : action.id,
                "name" : action.name
            ])
        }
        dict["actions"] = data
        
        app(app, runCommandName: "add_actions", payload: dict)
    }
    
    func openActionController() {
        app(app, tapButton: "Show Controller")
        app.swipeLeft()
    }
    
    func closeActionController() {
        app(app, tapButton: "Console Close Button")
        app.swipeLeft()
    }
    
    class Action {
        let id: Int
        let name: String
        
        init(id: Int, name: String) {
            self.id = id
            self.name = name
        }
    }
}

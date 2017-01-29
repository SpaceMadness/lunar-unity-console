//
//  ActionControllerTest.swift
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import XCTest

class VarInfo {
    let name: String
    let type: LUCVarType
    let value: String
    
    init(name: String, value: Any) {
        self.name = name
        self.type = VarInfo.getType(value: value)
        self.value = "\(value)"
    }
    
    static func getType(value: Any) -> LUCVarType {
        if value is Int {
            return LUCVarTypeInteger
        }
        if value is String {
            return LUCVarTypeString
        }
        if value is Double {
            return LUCVarTypeFloat
        }
        if value is Bool {
            return LUCVarTypeBoolean
        }
        XCTFail("Unexpected type \(value)")
        return LUCVarTypeUnknown
    }
}

class ActionControllerTest: UITestCaseBase {
    private let NO_ACTIONS: [String] = []
    private let NO_VARIABLES: [VarInfo] = []
    
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
        
        // Wait until the app is ready to receive commands
        waitForClientToConnect();
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
        app = nil
    }

    func testNoActions() {
        openActions();
        assertNoActions();
    }

    //region Actions

    func testRegisterActions() {
        registerActions(actions:
            Action(id: 1, name: "Action-1"),
            Action(id: 2, name: "Action-2"),
            Action(id: 3, name: "Action-3")
        )

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: NO_VARIABLES);
        closeActions();

        unregisterActions(actionIds: 2)

        openActions();
        assertEntries(actions: ["Action-1", "Action-3"], variables: NO_VARIABLES);
        closeActions();

        registerActions(actions:
            Action(id: 4, name: "Action-4"),
            Action(id: 5, name: "Action-2")
        )

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);
        closeActions();

        unregisterActions(actionIds: 1, 3, 4, 5)

        openActions();
        assertNoActions();
    }

    func testRegisterActionsWhileConsoleOpen() {
        openActions();
        assertNoActions();

        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-2")
        registerAction(actionId: 3, name: "Action-3")

        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: NO_VARIABLES);

        unregisterActions(actionIds: 2)

        assertEntries(actions: ["Action-1", "Action-3"], variables: NO_VARIABLES);

        registerAction(actionId: 4, name: "Action-4")
        registerAction(actionId: 5, name: "Action-2")

        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        unregisterActions(actionIds: 1, 3, 4, 5)

        assertNoActions();
    }

    func testFilter() {
        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-12")
        registerAction(actionId: 3, name: "Action-123")
        registerAction(actionId: 4, name: "Action-2")
        registerAction(actionId: 5, name: "Action-3")
        registerAction(actionId: 6, name: "Action-4")

        openActions();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        setFilter(text: "Action")
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        appendFilter(text: "-")
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        appendFilter(text: "1")
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: NO_VARIABLES);

        appendFilter(text: "2")
        assertEntries(actions: ["Action-12", "Action-123"], variables: NO_VARIABLES);

        appendFilter(text: "3")
        assertEntries(actions: ["Action-123"], variables: NO_VARIABLES);

        appendFilter(text: "4")
        assertEntries(actions: [], variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-123"], variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-12", "Action-123"], variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);
    }

    func testFilterAndAddRemoveActions() {
        registerActions(actions:
            Action(id: 1, name: "Action-1"),
            Action(id: 2, name: "Action-2"),
            Action(id: 3, name: "Action-3"),
            Action(id: 5, name: "Foo"))

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Foo"], variables: NO_VARIABLES);

        setFilter(text: "Action-1")
        assertEntries(actions: ["Action-1"], variables: NO_VARIABLES);

        registerAction(actionId: 4, name: "Action-12")
        assertEntries(actions: ["Action-1", "Action-12"], variables: NO_VARIABLES);

        unregisterActions(actionIds: 1)
        assertEntries(actions: ["Action-12"], variables: NO_VARIABLES);

        unregisterActions(actionIds: 4)
        assertEntries(actions: NO_ACTIONS, variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-2", "Action-3"], variables: NO_VARIABLES);

        unregisterActions(actionIds: 3)
        assertEntries(actions: ["Action-2"], variables: NO_VARIABLES);

        unregisterActions(actionIds: 2)
        assertEntries(actions: NO_ACTIONS, variables: NO_VARIABLES);

        unregisterActions(actionIds: 5)
        assertEntries(actions: NO_ACTIONS, variables: NO_VARIABLES);

        setFilter(text: "")
        assertNoActions();
    }

    func testFilterPersistence() {
        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-2")

        openActions();
        setFilter(text: "Action-1")
        appSearchButton(app).tap()
        assertEntries(actions: ["Action-1"], variables: NO_VARIABLES);
        closeActions();

        openActions();
        assertEntries(actions: ["Action-1"], variables: NO_VARIABLES);
    }

    func testTriggeringActions() {
        registerAction(actionId: 1, name: "Action")

        openActions();

//        makeNotificationCenterSync();
//        clickAction(0);
//
//        assertResult("console_open()", "console_action({id=1})");
        XCTFail("")
    }

    // MARK: Variables

    func testRegisterVariables() {
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);
        registerVariable(3, "float", 3.14);
        registerVariable(4, "boolean", false);

        openActions();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
    }

    func testRegisterVariablesWhileConsoleOpen() {
        openActions();
        assertNoActions();

        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);

        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        registerVariable(3, "float", 3.14);
        registerVariable(4, "boolean", false);

        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
    }

    func testVariablesFilter()
    {
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable-4", value: "value-4")
        ]);

        setFilter(text: "Variable")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable-4", value: "value-4")
        ]);

        appendFilter(text: "-")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable-4", value: "value-4")
        ]);

        appendFilter(text: "1")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilter(text: "2")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilter(text: "3")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilter(text: "4")
        assertEntries(actions: NO_ACTIONS, variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable-4", value: "value-4")
        ]);
    }

    func testVariablesFilterPersistence()
    {
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        setFilter(text: "Variable-1")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);
        closeActions();

        openActions();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);
    }

    func testUpdateVariables()
    {
        registerVariable(1, "string", "value", "value");

        openActions();

        // value should match
        app(app, textField: "Variable Input Field", assertText: "value")
        
        // change value
        app(app, textField: "Variable Input Field", enterText: "new value")
        
        // reset value
        app(app, tapButton: "Variable Reset Button")
        
        // value should match default value
        app(app, textField: "Variable Input Field", assertText: "value")
    }

    func testUpdateVariablesFromThePlugin()
    {
        registerVariable(1, "string", "value", "default value");

        openActions();

        // value should match
        app(app, textField: "Variable Input Field", assertText: "value")
        
        // set new value
        updateVariable(1, "new value")
        
        // value should match
        app(app, textField: "Variable Input Field", assertText: "new value")
    }

    func testUpdateVariablesFromThePluginWhileEditing()
    {
        registerVariable(1, "string", "value", "value");

        openActions();

        // value should match
        app(app, textField: "Variable Input Field", assertText: "value")
        
        // update variable
        
//        assertText(R.id.lunar_console_variable_entry_value, "value");
//        pressButton(R.id.lunar_console_variable_entry_value);
//
//        // ugly hack: sometimes pressing the "edit" button does not work for this test
//        int attempts = 10;
//        while (!isVisible(R.id.lunar_console_edit_variable_default_value) && attempts > 0)
//        {
//            pressButton(R.id.lunar_console_variable_entry_value);
//            --attempts;
//        }
//
//        ConsolePlugin.updateVariable(1, "another value");
//
//        assertText(R.id.lunar_console_edit_variable_default_value, String.format(getString(R.string.lunar_console_edit_variable_title_default_value), "default value"));
//        assertText(R.id.lunar_console_edit_variable_value, "value");
//        typeText(R.id.lunar_console_edit_variable_value, "new value");
//        pressButton(R.id.lunar_console_edit_variable_button_ok);
//
//        assertText(R.id.lunar_console_variable_entry_value, "new value");
//        pressButton(R.id.lunar_console_variable_entry_value);
//
//        assertText(R.id.lunar_console_edit_variable_value, "new value");
//        pressButton(R.id.lunar_console_edit_variable_button_reset);
//
//        assertText(R.id.lunar_console_variable_entry_value, "default value");
        
        XCTFail("")
    }

    //endregion

    //region Mixed

    func testRegisterActionsAndVariables() {
        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-2")
        registerAction(actionId: 3, name: "Action-3")
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);
        registerVariable(3, "float", 3.14);
        registerVariable(4, "boolean", false);

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
        closeActions();

        unregisterActions(actionIds: 2)

        openActions();
        assertEntries(actions: ["Action-1", "Action-3"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
        closeActions();

        registerAction(actionId: 4, name: "Action-4")
        registerAction(actionId: 5, name: "Action-2")

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
        closeActions();

        unregisterActions(actionIds: 1, 3, 4, 5)

        openActions();
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
    }

    func testRegisterActionsAndVariablesWhileConsoleOpen() {
        openActions();
        assertNoActions();

        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-2")
        registerAction(actionId: 3, name: "Action-3")
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);

        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: [
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        unregisterActions(actionIds: 2)

        assertEntries(actions: ["Action-1", "Action-3"], variables: [
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        registerAction(actionId: 4, name: "Action-4")
        registerAction(actionId: 5, name: "Action-2")
        registerVariable(3, "float", 3.14);
        registerVariable(4, "boolean", false);

        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        unregisterActions(actionIds: 1, 3, 4, 5)

        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
    }

    func testActionsAndVariablesFilter() {
        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-12")
        registerAction(actionId: 3, name: "Action-123")
        registerAction(actionId: 4, name: "Action-2")
        registerAction(actionId: 5, name: "Action-3")
        registerAction(actionId: 6, name: "Action-4")
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");

        openActions();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2")
        ]);

        appendFilter(text: "-")
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2")
        ]);

        appendFilter(text: "1")
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilter(text: "2")
        assertEntries(actions: ["Action-12", "Action-123"], variables: [
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilter(text: "3")
        assertEntries(actions: ["Action-123"], variables: [
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilter(text: "4")
        assertEntries(actions: NO_ACTIONS, variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-123"], variables: [
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-12", "Action-123"], variables: [
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2")
        ]);
    }

    func testFilterAndAddRemoveActionsWithVariables() {
        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-2")
        registerAction(actionId: 3, name: "Action-3")
        registerAction(actionId: 5, name: "Action5")
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-2", "value-2");
        registerVariable(3, "Variable-3", "value-3");
        registerVariable(5, "Variable5", "value5");

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action5"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable5", value: "value5"),
        ]);

        setFilter(text: "-1")
        assertEntries(actions: ["Action-1"], variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        registerAction(actionId: 4, name: "Action-12")
        assertEntries(actions: ["Action-1", "Action-12"], variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        unregisterActions(actionIds: 1)
        assertEntries(actions: ["Action-12"], variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        unregisterActions(actionIds: 4)
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-2", "Action-3"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        unregisterActions(actionIds: 3)
        assertEntries(actions: ["Action-2"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        unregisterActions(actionIds: 2)
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        unregisterActions(actionIds: 5)
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        setFilter(text: "")
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable5", value: "value5")
        ]);
    }

    func testFilterPersistenceWithActionsAndVariables() {
        registerAction(actionId: 1, name: "Action-1")
        registerAction(actionId: 2, name: "Action-12")
        registerAction(actionId: 3, name: "Action-123")
        registerAction(actionId: 4, name: "Action-2")
        registerAction(actionId: 5, name: "Action-3")
        registerAction(actionId: 6, name: "Action-4")
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        setFilter(text: "-1")
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: [
                VarInfo(name: "Variable-1", value: "value-1"),
                VarInfo(name: "Variable-12", value: "value-12"),
                VarInfo(name: "Variable-123", value: "value-123")
        ]);
        closeActions();

        openActions();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: [
                VarInfo(name: "Variable-1", value: "value-1"),
                VarInfo(name: "Variable-12", value: "value-12"),
                VarInfo(name: "Variable-123", value: "value-123")
        ]);
    }

    //endregion

    //region Test events

//    @Override
//    public void onTestEvent(String name, Object data)
//    {
//        if (name.equals(TEST_EVENT_NATIVE_CALLBACK))
//        {
//            Map<String, Object> payload = (Map<String, Object>) data;
//            String callback = (String) payload.get("name");
//            Map<String, Object> args = (Map<String, Object>) payload.get("arguments");
//            addResult(String.format("%s(%s)", callback, args != null ? args.toString() : ""));
//        }
//    }

    //endregion

    // MARK: Helpers

//    private void assertNoActions()
//    {
//        assertVisible(R.id.lunar_console_actions_warning_view);
//        assertHidden(R.id.lunar_console_action_view_list_container);
//    }
//
//    private void openActions()
//    {
//        openConsole();
//        findView(R.id.lunar_console_view_pager).perform(swipeLeft());
//    }
//
//    private void closeActions()
//    {
//        closeActions();
//    }
//
    func setFilter(text: String) {
        let filterSearchField = app.searchFields["Filter"];
        XCTAssertTrue(filterSearchField.exists);
        
        filterSearchField.coordinate(withNormalizedOffset: CGVector(dx: 0.5, dy: 0.5)).tap();
        appDeleteText(app)
        filterSearchField.typeText(text)
    }

    func appendFilter(text: String) {
        let filterSearchField = app.searchFields["Filter"];
        XCTAssertTrue(filterSearchField.exists);
        
        filterSearchField.coordinate(withNormalizedOffset: CGVector(dx: 0.5, dy: 0.5)).tap();
        filterSearchField.typeText(text)
    }

    func deleteLastFilterCharacter() {
        let filterSearchField = app.searchFields["Filter"];
        XCTAssertTrue(filterSearchField.exists);
        
        filterSearchField.coordinate(withNormalizedOffset: CGVector(dx: 0.5, dy: 0.5)).tap();
        appDeleteChar(app)
    }
//
//    private static void makeNotificationCenterSync()
//    {
//        try
//        {
//            Field dispatchQueueField = NotificationCenter.class.getDeclaredField("dispatchQueue");
//            dispatchQueueField.setAccessible(true);
//            dispatchQueueField.set(NotificationCenter.defaultCenter(), new SyncDispatchQueue());
//        }
//        catch (Exception e)
//        {
//            throw new AssertionError(e);
//        }
//    }
    
    // MARK: - Helpers
        
    func registerAction(actionId: Int, name: String) {
        var dict = Dictionary<String, Any>()
        var data = Array<Any>()
        data.append([
            "id" : actionId,
            "name" : name
        ])
        dict["actions"] = data
        
        app(app, runCommandName: "add_actions", payload: dict)
    }
    
    func registerActions(actions: Action...) {
        var dict = Dictionary<String, Any>()
        var data = Array<Any>()
        for action in actions {
            data.append(["id" : action.id, "name" : action.name])
        }
        dict["actions"] = data
        
        app(app, runCommandName: "add_actions", payload: dict)
    }
        
    func unregisterActions(actionIds: Int...) {
        var dict = Dictionary<String, Any>()
        dict["actions"] = actionIds
        app(app, runCommandName: "remove_actions", payload: dict)
    }
        
    func registerVariable(_ variableId: Int, _ name: String, _ value: Any) {
        registerVariable(variableId, name, value, value);
    }
        
    func registerVariable(_ variableId: Int, _ name: String, _ value: Any, _ defaultValue: Any) {
        var dict = Dictionary<String, Any>()
        var data = Array<Any>()
        data.append([
            "id" : variableId,
            "name" : name,
            "type" : getTypeName(value: value),
            "value" : "\(value)",
            "defaultValue": "\(defaultValue)"
        ])
        dict["variables"] = data
        
        app(app, runCommandName: "register_variable", payload: dict)
    }
    
    func updateVariable(_ variableId: Int, _ value: String) {
        var dict = Dictionary<String, Any>()
        var data = Array<Any>()
        data.append([
            "id" : variableId,
            "value" : value
            ])
        dict["variables"] = data
        
        app(app, runCommandName: "update_variable", payload: dict)
    }
        
    func assertNoActions() {
        XCTAssert(app.otherElements["No Actions Warning View"].isHittable);
    }
    
    func assertEntries(actions: [String], variables: [VarInfo]) {
        let table = app.tables.element;
        var expected = [String]()
        if actions.count > 0 {
            expected.append("Actions")
            expected.append(contentsOf: actions)
        }
        if variables.count > 0 {
            expected.append("Variables")
            for variable in variables {
                expected.append(variable.name)
            }
        }
        checkTable(table, items: expected)
    }
    
    func assertActions(actions: String...) {
        
        let table = app.tables.element;
        var expected = [String]()
        expected.append("Actions")
        expected.append(contentsOf: actions)
        checkTable(table, items: expected)
    }
    
    func addActions(actions: Action...) {
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
    
    func removeActions(actions: Int...) {
        var dict = Dictionary<String, Any>()
        dict["actions"] = actions
        app(app, runCommandName: "remove_actions", payload: dict)
    }
    
    func openActions() {
        app(app, tapButton: "Show Controller")
        app.swipeLeft()
    }
    
    func closeActions() {
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
        
    func getTypeName(value: Any) -> String {
        if value is Int {
            return LUCVarTypeNameInteger
        }
        if value is String {
            return LUCVarTypeNameString
        }
        if value is Double {
            return LUCVarTypeNameFloat
        }
        if value is Bool {
            return LUCVarTypeNameBoolean
        }
        XCTFail("Unexpected type \(value)")
        return LUCVarTypeNameUnknown
    }
}

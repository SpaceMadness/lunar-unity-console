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
    
    init(name: String, value: String) {
        self.name = name
        self.type = VarInfo.getType(value: value)
        self.value = value
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
    private static let NO_ACTIONS: [String] = []
    private static let NO_VARIABLES: [Any] = []
    
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

    func testNoActions() {
        openActions();
        assertNoActions();
    }

    //region Actions

    func testRegisterActions() {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: NO_VARIABLES);
        closeConsole();

        unregisterActions(2);

        openActions();
        assertEntries(actions: ["Action-1", "Action-3"], variables: NO_VARIABLES);
        closeActions();

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);
        closeActions();

        unregisterActions(1, 3, 4, 5);

        openActions();
        assertNoActions();
    }

    func testRegisterActionsWhileConsoleOpen() {
        openActions();
        assertNoActions();

        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");

        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: NO_VARIABLES);

        unregisterActions(2);

        assertEntries(actions: ["Action-1", "Action-3"], variables: NO_VARIABLES);

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        unregisterActions(1, 3, 4, 5);

        assertNoActions();
    }

    func testFilter() {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");

        openActions();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        setFilterText("Action");
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        appendFilterText("-");
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: NO_VARIABLES);

        appendFilterText("1");
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: NO_VARIABLES);

        appendFilterText("2");
        assertEntries(actions: ["Action-12", "Action-123"], variables: NO_VARIABLES);

        appendFilterText("3");
        assertEntries(actions: ["Action-123"], variables: NO_VARIABLES);

        appendFilterText("4");
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
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerAction(5, "Foo");

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Foo"], variables: NO_VARIABLES);

        setFilterText("Action-1");
        assertEntries(actions: ["Action-1"], variables: NO_VARIABLES);

        registerAction(4, "Action-12");
        assertEntries(actions: ["Action-1", "Action-12"], variables: NO_VARIABLES);

        unregisterActions(1);
        assertEntries(actions: ["Action-12"], variables: NO_VARIABLES);

        unregisterActions(4);
        assertEntries(actions: [], variables: NO_VARIABLES);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-2", "Action-3"], variables: NO_VARIABLES);

        unregisterActions(3);
        assertEntries(actions: ["Action-2"], variables: NO_VARIABLES);

        unregisterActions(2);
        assertEntries(actions: [], variables: NO_VARIABLES);

        unregisterActions(5);
        assertEntries(actions: [], variables: NO_VARIABLES);

        setFilterText("");
        assertNoActions();
    }

    func testFilterPersistence() {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");

        openActions();
        setFilterText("Action-1");
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: NO_VARIABLES);
        closeActions();

        openActions();
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: NO_VARIABLES);
    }

    func testTriggeringActions() {
        registerAction(1, "Action");

        openActions();

        makeNotificationCenterSync();
        clickAction(0);

        assertResult("console_open()", "console_action({id=1})");
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

        setFilterText("Variable");
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable-4", value: "value-4")
        ]);

        appendFilterText("-");
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable-4", value: "value-4")
        ]);

        appendFilterText("1");
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilterText("2");
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilterText("3");
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilterText("4");
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
        setFilterText("Variable-1");
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
        registerVariable(1, "string", "value", "default value");

        openActions();

        assertText(R.id.lunar_console_variable_entry_value, "value");
        pressButton(R.id.lunar_console_variable_entry_value);

        // ugly hack: sometimes pressing the "edit" button does not work for this test
        int attempts = 10;
        while (!isVisible(R.id.lunar_console_edit_variable_default_value) && attempts > 0)
        {
            pressButton(R.id.lunar_console_variable_entry_value);
            --attempts;
        }

        assertText(R.id.lunar_console_edit_variable_default_value, String.format(getString(R.string.lunar_console_edit_variable_title_default_value), "default value"));
        assertText(R.id.lunar_console_edit_variable_value, "value");
        typeText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_ok);

        assertText(R.id.lunar_console_variable_entry_value, "new value");
        pressButton(R.id.lunar_console_variable_entry_value);

        assertText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_reset);

        assertText(R.id.lunar_console_variable_entry_value, "default value");
    }

    func testUpdateVariablesFromThePlugin()
    {
        registerVariable(1, "string", "value", "default value");

        openActions();

        assertText(R.id.lunar_console_variable_entry_value, "value");
        pressButton(R.id.lunar_console_variable_entry_value);

        ConsolePlugin.updateVariable(1, "new value");

        assertText(R.id.lunar_console_variable_entry_value, "new value");
    }

    func testUpdateVariablesFromThePluginWhileEditing()
    {
        registerVariable(1, "string", "value", "default value");

        openActions();

        assertText(R.id.lunar_console_variable_entry_value, "value");
        pressButton(R.id.lunar_console_variable_entry_value);

        // ugly hack: sometimes pressing the "edit" button does not work for this test
        int attempts = 10;
        while (!isVisible(R.id.lunar_console_edit_variable_default_value) && attempts > 0)
        {
            pressButton(R.id.lunar_console_variable_entry_value);
            --attempts;
        }

        ConsolePlugin.updateVariable(1, "another value");

        assertText(R.id.lunar_console_edit_variable_default_value, String.format(getString(R.string.lunar_console_edit_variable_title_default_value), "default value"));
        assertText(R.id.lunar_console_edit_variable_value, "value");
        typeText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_ok);

        assertText(R.id.lunar_console_variable_entry_value, "new value");
        pressButton(R.id.lunar_console_variable_entry_value);

        assertText(R.id.lunar_console_edit_variable_value, "new value");
        pressButton(R.id.lunar_console_edit_variable_button_reset);

        assertText(R.id.lunar_console_variable_entry_value, "default value");
    }

    //endregion

    //region Mixed

    func testRegisterActionsAndVariables() {
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
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
        closeConsole();

        unregisterActions(2);

        openActions();
        assertEntries(actions: ["Action-1", "Action-3"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
        closeActions();

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");

        openActions();
        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
        closeActions();

        unregisterActions(1, 3, 4, 5);

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

        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerVariable(1, "string", "value");
        registerVariable(2, "integer", 10);

        assertEntries(actions: ["Action-1", "Action-2", "Action-3"], variables: [
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        unregisterActions(2);

        assertEntries(actions: ["Action-1", "Action-3"], variables: [
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        registerAction(4, "Action-4");
        registerAction(5, "Action-2");
        registerVariable(3, "float", 3.14);
        registerVariable(4, "boolean", false);

        assertEntries(actions: ["Action-1", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);

        unregisterActions(1, 3, 4, 5);

        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "boolean", value: false),
            VarInfo(name: "float", value: 3.14),
            VarInfo(name: "integer", value: 10),
            VarInfo(name: "string", value: "value")
        ]);
    }

    func testActionsAndVariablesFilter() {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");
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

        appendFilterText("-");
        assertEntries(actions: ["Action-1", "Action-12", "Action-123", "Action-2", "Action-3", "Action-4"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123"),
            VarInfo(name: "Variable-2", value: "value-2")
        ]);

        appendFilterText("1");
        assertEntries(actions: ["Action-1", "Action-12", "Action-123"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilterText("2");
        assertEntries(actions: ["Action-12", "Action-123"], variables: [
            VarInfo(name: "Variable-12", value: "value-12"),
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilterText("3");
        assertEntries(actions: ["Action-123"], variables: [
            VarInfo(name: "Variable-123", value: "value-123")
        ]);

        appendFilterText("4");
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
        registerAction(1, "Action-1");
        registerAction(2, "Action-2");
        registerAction(3, "Action-3");
        registerAction(5, "Action5");
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

        setFilterText("-1");
        assertEntries(actions: ["Action-1"], variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        registerAction(4, "Action-12");
        assertEntries(actions: ["Action-1", "Action-12"], variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        unregisterActions(1);
        assertEntries(actions: ["Action-12"], variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        unregisterActions(4);
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1")
        ]);

        deleteLastFilterCharacter();
        assertEntries(actions: ["Action-2", "Action-3"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        unregisterActions(3);
        assertEntries(actions: ["Action-2"], variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        unregisterActions(2);
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        unregisterActions(5);
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3")
        ]);

        setFilterText("");
        assertEntries(actions: NO_ACTIONS, variables: [
            VarInfo(name: "Variable-1", value: "value-1"),
            VarInfo(name: "Variable-2", value: "value-2"),
            VarInfo(name: "Variable-3", value: "value-3"),
            VarInfo(name: "Variable5", value: "value5")
        ]);
    }

    func testFilterPersistenceWithActionsAndVariables() {
        registerAction(1, "Action-1");
        registerAction(2, "Action-12");
        registerAction(3, "Action-123");
        registerAction(4, "Action-2");
        registerAction(5, "Action-3");
        registerAction(6, "Action-4");
        registerVariable(1, "Variable-1", "value-1");
        registerVariable(2, "Variable-12", "value-12");
        registerVariable(3, "Variable-123", "value-123");
        registerVariable(4, "Variable-2", "value-2");
        registerVariable(5, "Variable-3", "value-3");
        registerVariable(6, "Variable-4", "value-4");

        openActions();
        setFilterText("-1");
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
//        closeConsole();
//    }
//
//    private void setFilterText(String filterText)
//    {
//        typeText(R.id.lunar_console_action_view_text_edit_filter, filterText);
//    }
//
//    private void appendFilterText(String filterText)
//    {
//        appendText(R.id.lunar_console_action_view_text_edit_filter, filterText);
//    }
//
//    private void deleteLastFilterCharacter()
//    {
//        deleteLastChar(R.id.lunar_console_action_view_text_edit_filter);
//    }
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
    
    // MARK: - Tests
    
    func testNoAction() {
        // open controller
        openActionController()
        
        // should be no actions
        assertNoActions()
    }
    
    func testActionOperations() {
        // add a few actions
        addActions(actions: Action(id: 1, name: "Action 1"),
                   Action(id: 2, name: "Action 2"),
                   Action(id: 3, name: "Action 3"))
        
        // open controller
        openActionController()
        
        // check actions
        assertActions(actions: "Action 1", "Action 2", "Action 3")
        
        // close controller
        closeActionController()
        
        // add more actions
        addActions(actions: Action(id: 4, name: "Action 4"))
        
        // open controller
        openActionController()
        
        // check actions
        assertActions(actions: "Action 1", "Action 2", "Action 3", "Action 4")
        
        // close controller
        closeActionController()
        
        // remove some actions
        removeActions(actions: 2, 3)
        
        // open controller
        openActionController()
        
        // check actions
        assertActions(actions: "Action 1", "Action 4")
        
        // close controller
        closeActionController()
        
        // remove some actions
        removeActions(actions: 1, 4)
        
        // open controller
        openActionController()
        
        // should be no actions
        assertNoActions()
    }
    
    func testActionOperationsWithConsoleOpen() {
        
        // open controller
        openActionController()
        
        // add a few actions
        addActions(actions: Action(id: 1, name: "Action 1"),
                   Action(id: 2, name: "Action 2"),
                   Action(id: 3, name: "Action 3"))
        
        
        
        // check actions
        assertActions(actions: "Action 1", "Action 2", "Action 3")
        
        // add more actions
        addActions(actions: Action(id: 4, name: "Action 4"))
        
        // check actions
        assertActions(actions: "Action 1", "Action 2", "Action 3", "Action 4")
        
        // remove some actions
        removeActions(actions: 2, 3)
        
        // check actions
        assertActions(actions: "Action 1", "Action 4")
        
        // remove some actions
        removeActions(actions: 1, 4)
        
        // should be no actions
        assertNoActions()
    }
    
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
        
    func unregisterActions(actionIds: Int...) {
        var dict = Dictionary<String, Any>()
        dict["actions"] = actionIds
        app(app, runCommandName: "remove_actions", payload: dict)
    }
        
    func registerVariable(variableId: Int, name: String, value: Any) {
        registerVariable(variableId: variableId, name: name, value: value, defaultValue: value);
    }
        
    func registerVariable(variableId: Int, name: String, value: Any, defaultValue: Any) {
        var dict = Dictionary<String, Any>()
        var data = Array<Any>()
        data.append([
            "id" : variableId,
            "name" : name,
            "type" : getTypeName(value: value),
            "value" : "\(value)",
            "defaultValue": "\(defaultValue)"
        ])
        dict["variable"] = data
        
        app(app, runCommandName: "register_variable", payload: dict)
    }
        
    func assertNoActions() {
        XCTAssert(app.otherElements["No Actions Warning View"].isHittable);
    }
    
    func assertEntries(actions: [String], variables: [VarInfo]) {
        XCTFail("")
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

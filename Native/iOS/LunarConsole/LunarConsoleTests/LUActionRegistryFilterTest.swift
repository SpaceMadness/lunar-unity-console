//
//  LUActionRegistryFilterTest.swift
//  LunarConsole
//
//  Created by Alex Lementuev on 12/16/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import XCTest

class LUEntryInfo {
    let name: String
    
    init(name: String) {
        self.name = name
    }
    
    func isEqual(entry: LUEntry) -> Bool {
        return false
    }
}

class LUActionInfo : LUEntryInfo {
    override func isEqual(entry: LUEntry) -> Bool {
        if let entry = entry as? LUAction {
            return entry.name == self.name
        }
        return false
    }
}

class LUCVarInfo :  LUEntryInfo {
    let value: String
    let type: String

    init(name: String, value: String, type: String) {
        self.value = value
        self.type = type
        
        super.init(name: name)
    }
    
    override func isEqual(entry: LUEntry) -> Bool {
        if let cvar = entry as? LUCVar {
            return self.name == cvar.name &&
                   self.value == cvar.value &&
                   self.type == LUCVar.typeName(for: cvar.type)
        }
        
        return false
    }
}

class LUActionRegistryFilterTest: TestCase, LUActionRegistryFilterDelegate {
    
    var _nextActionId: Int32 = 0
    var _actionRegistry: LUActionRegistry!
    var _registryFilter: LUActionRegistryFilter!
    
    // MARK: - Setup
    
    override func setUp() {
        super.setUp()
        
        _actionRegistry = LUActionRegistry()
        _registryFilter = LUActionRegistryFilter(actionRegistry: _actionRegistry)
        _registryFilter.delegate = self
        _nextActionId = 0
    }
    
    // MARK: - Filter by text

    func testFilterByText() {
        registerEntries(entries:
          LUActionInfo(name: "line1"),
          LUActionInfo(name: "line11"),
          LUActionInfo(name: "line111"),
          LUActionInfo(name: "line1111"),
          LUActionInfo(name: "foo"),
          LUCVarInfo(name: "line1", value: "value", type: LUCVarTypeNameString),
          LUCVarInfo(name: "line11", value: "value", type: LUCVarTypeNameString),
          LUCVarInfo(name: "line111", value: "value", type: LUCVarTypeNameString),
          LUCVarInfo(name: "line1111", value: "value", type: LUCVarTypeNameString),
          LUCVarInfo(name: "foo", value: "value", type: LUCVarTypeNameString)
        )
        
        assertNotFiltering()
        
        XCTAssert(setFilter(text: "l"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(!setFilter(text: "l"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "li"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "lin"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line1"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line11"))
        assertActions(names: "line11", "line111", "line1111")
        assertVariables(names: "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line111"))
        assertActions(names: "line111", "line1111")
        assertVariables(names: "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line1111"))
        assertActions(names: "line1111")
        assertVariables(names: "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line11111"))
        assertNoActions();
        assertFiltering()
        
        XCTAssert(setFilter(text: "line1111"))
        assertActions(names: "line1111")
        assertVariables(names: "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line111"))
        assertActions(names: "line111", "line1111")
        assertVariables(names: "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line11"))
        assertActions(names: "line11", "line111", "line1111")
        assertVariables(names: "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line1"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "line"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "lin"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "li"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: "l"))
        assertActions(names: "line1", "line11", "line111", "line1111")
        assertVariables(names: "line1", "line11", "line111", "line1111")
        assertFiltering()
        
        XCTAssert(setFilter(text: ""))
        assertActions(names: "foo", "line1", "line11", "line111", "line1111")
        assertVariables(names: "foo", "line1", "line11", "line111", "line1111")
        assertNotFiltering()
    }

    // MARK: - Register entries

    func testRegisterEntries() {
        registerAction(name: "a11")
        registerVariable(name: "v11")
        
        registerAction(name: "a1")
        registerVariable(name: "v1")
        
        registerAction(name: "a111")
        registerVariable(name: "v111")
        
        assertActions(names: "a1", "a11", "a111")
        assertVariables(names: "v1", "v11", "v111")
    }

    func testRegisterEntriesFiltered() {
        setFilter(text: "a11")
        
        registerAction(name: "a11")
        registerAction(name: "a1")
        registerAction(name: "a111")
        
        assertActions(names: "a11", "a111")
        assertNoVariables()
        
        registerVariable(name: "v11")
        registerVariable(name: "v1")
        registerVariable(name: "v111")
        
        setFilter(text: "v11")
        
        assertNoActions()
        assertVariables(names: "v11", "v111")
        
        // remove the filter
        setFilter(text: "")
        
        assertActions(names: "a1", "a11", "a111")
        assertVariables(names: "v1", "v11", "v111")
    }

    func testRegisterMultipleActionsWithSameName() {
        registerAction(name: "a2")
        registerAction(name: "a3")
        registerAction(name: "a1")
        registerAction(name: "a3")
        
        assertActions(names: "a1", "a2", "a3")
    }
    
    func testRegisterMultipleActionsWithSameNameFiltered() {
        setFilter(text: "a1");
        assertFiltering()
        
        registerAction(name: "a")
        registerAction(name: "a11")
        registerAction(name: "a12")
        registerAction(name: "a11")
        
        assertActions(names: "a11", "a12")
        
        setFilter(text: "a");
        assertFiltering()
        assertActions(names: "a", "a11", "a12")
        
        setFilter(text: "");
        assertNotFiltering()
        assertActions(names: "a", "a11", "a12")
    }

    // MARK: - Unregister actions

    func testUnregisterActions() {
        let id2 = registerAction(name: "a2").actionId
        let id1 = registerAction(name: "a1").actionId
        let id3 = registerAction(name: "a3").actionId
        
        unregisterAction(id: id1)
        assertActions(names: "a2", "a3")
        
        unregisterAction(id: id2)
        assertActions(names: "a3")
        
        unregisterAction(id: id3)
        assertNoActions()
        
        unregisterAction(id: id3)
        assertNoActions()
    }

    func testUnregisterActionFiltered() {
        setFilter(text: "a11")
        
        let id2 = registerAction(name: "a11").actionId
        let id1 = registerAction(name: "a1").actionId
        let id3 = registerAction(name: "a111").actionId
        
        unregisterAction(id: id1)
        assertActions(names: "a11", "a111")
        
        unregisterAction(id: id2)
        assertActions(names: "a111")
        
        unregisterAction(id: id3)
        assertNoActions()
        
        unregisterAction(id: id3)
        assertNoActions()
    }

    // MARK: - Delegate notifications

    func testDelegateNotifications() {
        // register actions
        registerAction(name: "a2")
        
        assertResult(expected: "added action: a2 (0)")
        
        registerAction(name: "a1")
        assertResult(expected: "added action: a1 (0)")
        
        registerAction(name: "a3")
        assertResult(expected: "added action: a3 (2)")
        
        // register variables
        registerVariable(name: "1.bool", typeName: LUCVarTypeNameBoolean, value: "1")
        assertResult(expected: "register variable: Boolean 1.bool 1 (0)")
        
        registerVariable(name: "2.int", typeName: LUCVarTypeNameInteger, value: "10")
        assertResult(expected: "register variable: Integer 2.int 10 (1)")
        
        registerVariable(name: "3.float", typeName: LUCVarTypeNameFloat, value: "3.14")
        assertResult(expected: "register variable: Float 3.float 3.14 (2)")
        
        registerVariable(name: "4.string", typeName: LUCVarTypeNameString, value: "value")
        assertResult(expected: "register variable: String 4.string value (3)")
        
        // unregister variables
        unregisterAction(name: "a1")
        assertResult(expected: "removed action: a1 (0)")
        
        unregisterAction(name: "a3")
        assertResult(expected: "removed action: a3 (1)")
        
        unregisterAction(name: "a2")
        assertResult(expected: "removed action: a2 (0)")
    }

    func testDelegateNotificationsFiltered() {
        // set filter
        setFilter(text: "a1")
        
        // register actions
        registerAction(name: "a")
        
        registerAction(name: "a11")
        assertResult(expected: "added action: a11 (0)")
        
        registerAction(name: "a12")
        assertResult(expected: "added action: a12 (1)")
        
        // register variables
        registerVariable(name: "a", typeName: LUCVarTypeNameBoolean, value: "1")
        
        registerVariable(name: "a1", typeName: LUCVarTypeNameInteger, value: "10")
        assertResult(expected: "register variable: Integer a1 10 (0)")
        
        registerVariable(name: "a12", typeName: LUCVarTypeNameFloat, value: "3.14")
        assertResult(expected: "register variable: Float a12 3.14 (1)")
        
        registerVariable(name: "a13", typeName: LUCVarTypeNameString, value: "value")
        assertResult(expected: "register variable: String a13 value (2)")
        
        // unregister variables
        unregisterAction(name: "a1")
        
        unregisterAction(name: "a11")
        assertResult(expected: "removed action: a11 (0)")
        
        unregisterAction(name: "a12")
        assertResult(expected: "removed action: a12 (0)")
    }

    func testFilteringByTextAddActions() {
        assertNotFiltering()
        
        XCTAssert(setFilter(text: "line11"))
        registerAction(id: 0, name: "line111")
        
        assertActions(names: "line111")
        assertFiltering()
        
        registerAction(id: 1, name: "line1")
        registerAction(id: 2, name: "line11")
        
        assertActions(names: "line11", "line111")
        
        unregisterAction(id: 2)
        assertActions(names: "line111")
        
        unregisterAction(id: 1)
        assertActions(names: "line111")
        
        unregisterAction(id: 0)
        assertNoActions()
        
        registerAction(id: 3, name: "line1")
        assertNoActions()
        
        registerAction(id: 4, name: "line11")
        assertActions(names: "line11")
        
        setFilter(text: "")
        assertActions(names: "line1", "line11")
    }

    // MARK: - LUActionRegistryFilterDelegate
    
    func actionRegistryFilter(_ registryFilter: LUActionRegistryFilter!, didAdd action: LUAction!, at index: UInt) {
        addResult("added action: \(action.name!) (\(index))")
    }

    func actionRegistryFilter(_ registryFilter: LUActionRegistryFilter!, didRemove action: LUAction!, at index: UInt) {
        addResult("removed action: \(action.name!) (\(index))")
    }
    
    func actionRegistryFilter(_ registry: LUActionRegistryFilter!, didRegisterVariable variable: LUCVar!, at index: UInt) {
        addResult("register variable: \(LUCVar.typeName(for: variable.type)!) \(variable.name!) \(variable.value!) (\(index))")
    }
    
    func actionRegistryFilter(_ registry: LUActionRegistryFilter!, didChangeVariable variable: LUCVar!, at index: UInt) {
        XCTFail("Implement me")
    }
    
    // MARK: - Helpers
    
    @discardableResult
    func setFilter(text: String) -> Bool {
        return _registryFilter.setFilterText(text)
    }
    
    @discardableResult
    func registerAction(name: String) -> LUAction {
        _nextActionId = _nextActionId + 1
        return registerAction(id: _nextActionId, name: name)
    }
    
    @discardableResult
    func registerAction(id: Int32, name: String) -> LUAction {
        return _actionRegistry.registerAction(withId: id, name: name)
    }
    
    @discardableResult
    func registerVariable(name: String, typeName: String, value: String) -> LUCVar {
        _nextActionId = _nextActionId + 1
        return _actionRegistry.registerVariable(withId: _nextActionId, name: name, typeName: typeName, value: value, defaultValue: value)
    }
    
    @discardableResult
    func registerVariable(name: String) -> LUCVar {
        _nextActionId = _nextActionId + 1
        return _actionRegistry.registerVariable(withId: _nextActionId, name: name, typeName: LUCVarTypeNameString, value: "value", defaultValue: "value")
    }
    
    @discardableResult
    func unregisterAction(name: String) -> Bool {
        for i in 0..<_actionRegistry.actions.count {
            let action = _actionRegistry.actions[i] as! LUAction
            if action.name == name {
                _actionRegistry.unregisterAction(withId: action.actionId)
                return true
            }
        }
        
        return false
    }
    
    func unregisterAction(id: Int32) {
        _actionRegistry.unregisterAction(withId: id)
    }
    
    func registerEntries(entries: LUEntryInfo...) {
        for info in entries {
            _nextActionId = _nextActionId + 1
            
            if let action = info as? LUActionInfo {
                _actionRegistry.registerAction(withId: _nextActionId, name: action.name)
            } else if let cvar = info as? LUCVarInfo {
                _actionRegistry.registerVariable(withId: _nextActionId, name: cvar.name, typeName: cvar.type, value: cvar.value, defaultValue: cvar.value)
            } else {
                abort() // not the best solution but better than ignoring
            }
        }
    }

    // MARK: - Assertion Helpers
    
    func assertResult(expected: String...) {
        let message = "Expected: '\(expected.joined(separator: ","))' but was '\(self.result.componentsJoined(by: ","))'"
        
        XCTAssertEqual(expected.count, self.result.count, message)
        for i in 0..<expected.count {
            XCTAssertEqual(expected[i], self.result[i] as! String, message)
        }
        
        self.result.removeAllObjects()
    }
    
    func assertNoActions() {
        XCTAssert(_registryFilter.actions.count == 0)
    }
    
    func assertNoVariables() {
        XCTAssert(_registryFilter.variables.count == 0)
    }
    
    func assertActions(names: String...) {
        var actualNames = [String]()
        for i in 0..<_registryFilter.actions.count {
            let action = _registryFilter.actions[i] as! LUAction
            actualNames.append(action.name)
        }
        
        XCTAssertEqual(names.count, actualNames.count, "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        
        for i in 0..<actualNames.count {
            XCTAssertEqual(names[i], actualNames[i], "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        }
    }

    func assertVariables(names: String...) {
        var actualNames = [String]()
        for i in 0..<_registryFilter.variables.count {
            let action = _registryFilter.variables[i] as! LUCVar
            actualNames.append(action.name)
        }
        
        XCTAssertEqual(names.count, actualNames.count, "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        
        for i in 0..<actualNames.count {
            XCTAssertEqual(names[i], actualNames[i], "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        }
    }
    
    func assertFiltering() {
        XCTAssertTrue(_registryFilter.isFiltering)
    }
    
    func assertNotFiltering() {
        XCTAssertFalse(_registryFilter.isFiltering)
    }
}

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
        let filter = createFilter(entries:
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
        
        XCTAssert(!filter.isFiltering)
        
        XCTAssert(filter.setFilterText("l"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(!filter.setFilterText("l"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("li"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("lin"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line1"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line11"))
        assertActions(filter: filter, names: "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line111"))
        assertActions(filter: filter, names: "line111", "line1111")
        assertVariables(filter: filter, names: "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line1111"))
        assertActions(filter: filter, names: "line1111")
        assertVariables(filter: filter, names: "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line11111"))
        assertNoActions(filter: filter);
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line1111"))
        assertActions(filter: filter, names: "line1111")
        assertVariables(filter: filter, names: "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line111"))
        assertActions(filter: filter, names: "line111", "line1111")
        assertVariables(filter: filter, names: "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line11"))
        assertActions(filter: filter, names: "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line1"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("line"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("lin"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("li"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText("l"))
        assertActions(filter: filter, names: "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "line1", "line11", "line111", "line1111")
        XCTAssert(filter.isFiltering)
        
        XCTAssert(filter.setFilterText(""))
        assertActions(filter: filter, names: "foo", "line1", "line11", "line111", "line1111")
        assertVariables(filter: filter, names: "foo", "line1", "line11", "line111", "line1111")
        XCTAssert(!filter.isFiltering)
    }

    // MARK: - Register entries

    func testRegisterEntries() {
        registerAction(name: "a11")
        registerVariable(name: "v11")
        
        registerAction(name: "a1")
        registerVariable(name: "v1")
        
        registerAction(name: "a111")
        registerVariable(name: "v111")
        
        assertActions(filter: _registryFilter, names: "a1", "a11", "a111")
        assertVariables(filter: _registryFilter, names: "v1", "v11", "v111")
    }

    func testRegisterEntriesFiltered() {
        setFilter(text: "a11")
        
        registerAction(name: "a11")
        registerAction(name: "a1")
        registerAction(name: "a111")
        
        assertActions(filter: _registryFilter, names: "a11", "a111")
        assertNoVariables(filter: _registryFilter)
        
        registerVariable(name: "v11")
        registerVariable(name: "v1")
        registerVariable(name: "v111")
        
        setFilter(text: "v11")
        
        assertNoActions(filter: _registryFilter)
        assertVariables(filter: _registryFilter, names: "v11", "v111")
        
        // remove the filter
        setFilter(text: "")
        
        assertActions(filter: _registryFilter, names: "a1", "a11", "a111")
        assertVariables(filter: _registryFilter, names: "v1", "v11", "v111")
    }

    func testRegisterMultipleActionsWithSameName() {
        registerAction(name: "a2")
        registerAction(name: "a3")
        registerAction(name: "a1")
        registerAction(name: "a3")
        
        // should be in default group
        assertActions(filter: _registryFilter, names: "a1", "a2", "a3")
    }

    // MARK: - Unregister actions

    func testUnregisterActions() {
        let id2 = registerAction(name: "a2").actionId
        let id1 = registerAction(name: "a1").actionId
        let id3 = registerAction(name: "a3").actionId
        
        unregisterAction(id: id1)
        assertActions(filter: _registryFilter, names: "a2", "a3")
        
        unregisterAction(id: id2)
        assertActions(filter: _registryFilter, names: "a3")
        
        unregisterAction(id: id3)
        assertNoActions(filter: _registryFilter)
        
        unregisterAction(id: id3)
        assertNoActions(filter: _registryFilter)
    }

    func testUnregisterActionFiltered()
    {
        setFilter(text: "a11")
        
        let id2 = registerAction(name: "a11").actionId
        let id1 = registerAction(name: "a1").actionId
        let id3 = registerAction(name: "a111").actionId
        
        unregisterAction(id: id1)
        assertActions(filter: _registryFilter, names: "a11", "a111")
        
        unregisterAction(id: id2)
        assertActions(filter: _registryFilter, names: "a111")
        
        unregisterAction(id: id3)
        assertNoActions(filter: _registryFilter)
        
        unregisterAction(id: id3)
        assertNoActions(filter: _registryFilter)
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

    func testDelegateNotificationsFiltered()
    {
        // XCTFail("Implement me")
    }

    func testFilteringByTextAddActions()
    {
    //    LUActionRegistryFilter *filter = createFilter(nil)
    //    XCTAssert(!filter.isFiltering)
    //    
    //    XCTAssert(filter.setFilterText("line11"))
    //    [filter.registry registerActionWithId:0 name:"line111" group:""]
    //    
    //    assertActions(filter: _registryFilter, names: "line111")
    //    XCTAssert(filter.isFiltering)
    //    
    //    [filter.registry registerActionWithId:1 name:"line1" group:""]
    //    [filter.registry registerActionWithId:2 name:"line11" group:""]
    //    
    //    assertActions(filter: _registryFilter, names: "line11", "line111")
    //    
    //    [filter.registry unregisterActionWithId:2]
    //    assertActions(filter: _registryFilter, names: "line111")
    //    
    //    [filter.registry unregisterActionWithId:1]
    //    assertActions(filter: _registryFilter, names: "line111")
    //    
    //    [filter.registry unregisterActionWithId:0]
    //    [self assertFilterGroups:filter, nil]
    //    
    //    [filter.registry registerActionWithId:3 name:"line1" group:"a"]
    //    [self assertFilterGroups:filter, nil]
    //    
    //    [filter.registry registerActionWithId:4 name:"line11" group:"a"]
    //    [self assertFilterGroups:filter, [LUActionGroupInfo groupInfoWithName:"a" actions:"line11", nil], nil]
    //    
    //    filter.setFilterText("")
    //    [self assertFilterGroups:filter, [LUActionGroupInfo groupInfoWithName:"a" actions:"line1", "line11", nil], nil]
        
        // XCTFail("Implement me")
    }

    // MARK: - LUActionRegistryFilterDelegate
    
    func actionRegistryFilter(_ registryFilter: LUActionRegistryFilter!, didAdd action: LUAction!, at index: UInt) {
        addResult("added action: \(action.name) (\(index))")
    }

    func actionRegistryFilter(_ registryFilter: LUActionRegistryFilter!, didRemove action: LUAction!, at index: UInt) {
        addResult("removed action: \(action.name) (\(index))")
    }
    
    func actionRegistryFilter(_ registry: LUActionRegistryFilter!, didRegisterVariable variable: LUCVar!, at index: UInt) {
        addResult("register variable: \(LUCVar.typeName(for: variable.type)) \(variable.name) \(variable.value), (\(index))")
    }
    
    func actionRegistryFilter(_ registry: LUActionRegistryFilter!, didChangeVariable variable: LUCVar!, at index: UInt) {
        XCTFail("Implement me")
    }

    // MARK: - Helpers

    func setFilter(text: String) {
        _registryFilter.setFilterText(text)
    }
    
    func assertResult(expected: String...) {
        
    }
    
    func assertNoActions(filter: LUActionRegistryFilter) {
        XCTAssert(filter.actions.count == 0)
    }
    
    func assertNoVariables(filter: LUActionRegistryFilter) {
        XCTAssert(filter.variables.count == 0)
    }
    
    func assertActions(filter: LUActionRegistryFilter, names: String...) {
        var actualNames = [String]()
        for i in 0..<filter.actions.count {
            let action = filter.actions[i] as! LUAction
            actualNames.append(action.name)
        }
        
        XCTAssertEqual(names.count, actualNames.count, "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        
        for i in 0..<actualNames.count {
            XCTAssertEqual(names[i], actualNames[i], "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        }
    }

    func assertVariables(filter: LUActionRegistryFilter, names: String...) {
        var actualNames = [String]()
        for i in 0..<filter.variables.count {
            let action = filter.variables[i] as! LUCVar
            actualNames.append(action.name)
        }
        
        XCTAssertEqual(names.count, actualNames.count, "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        
        for i in 0..<actualNames.count {
            XCTAssertEqual(names[i], actualNames[i], "Expected \(names.joined(separator: ",")) but was \(actualNames.joined(separator: ","))")
        }
    }

    func assertFilterEntries(filter: LUActionRegistryFilter, entries: LUEntryInfo) {
        XCTFail()
        /*
        va_list ap
        va_start(ap, filter)
        NSMutableArray *expectedActions = [NSMutableArray array]
        NSMutableArray *expectedVariable = [NSMutableArray array]
        for (LUActionInfo *info = va_arg(ap, LUActionInfo *) info != nil info = va_arg(ap, LUActionInfo *))
        {
            if ([info isKindOfClass:[LUActionInfo class]])
            {
                [expectedActions addObject:info]
            }
            else if ([info isKindOfClass:[LUCVarInfo class]])
            {
                [expectedVariable addObject:info]
            }
        }
        va_end(ap)
        
        XCTAssertEqual(expectedActions.count, filter.actions.count, "Expected [%@] but was [%@]", [expectedActions componentsJoinedByString:","], [filter.actions componentsJoinedByString:","])
        for (NSInteger i = 0 i < filter.actions.count ++i)
        {
            LUActionInfo *info = expectedActions[i]
            XCTAssertTrue([info isEqualToEntry:filter.actions[i]], "Expected [%@] but was [%@]", [expectedActions componentsJoinedByString:","], [filter.actions componentsJoinedByString:","])
        }
        */
    }

    func registerAction(name: String) -> LUAction {
        _nextActionId = _nextActionId + 1
        return _actionRegistry.registerAction(withId: _nextActionId, name: name)
    }

    func registerVariable(name: String, typeName: String, value: String) -> LUCVar {
        _nextActionId = _nextActionId + 1
        return _actionRegistry.registerVariable(withId: _nextActionId, name: name, typeName: typeName, value: value, defaultValue: value)
    }

    func registerVariable(name: String) -> LUCVar {
        _nextActionId = _nextActionId + 1
        return _actionRegistry.registerVariable(withId: _nextActionId, name: name, typeName: LUCVarTypeNameString, value: "value", defaultValue: "value")
    }

    func unregisterAction(name: String) -> Bool {
        for i in 0..<_actionRegistry.actions.count - 1 {
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

    func createFilter(entries: LUEntryInfo...) -> LUActionRegistryFilter {
        let registry = LUActionRegistry()
        
        for info in entries {
            _nextActionId = _nextActionId + 1
            
            if let action = info as? LUActionInfo {
                registry.registerAction(withId: _nextActionId, name: action.name)
            } else if let cvar = info as? LUCVarInfo {
                registry.registerVariable(withId: _nextActionId, name: cvar.name, typeName: cvar.type, value: cvar.value, defaultValue: cvar.value)
            } else {
                abort() // not the best solution but better than ignoring
            }
        }
        
        return LUActionRegistryFilter(actionRegistry: registry)
    }
}

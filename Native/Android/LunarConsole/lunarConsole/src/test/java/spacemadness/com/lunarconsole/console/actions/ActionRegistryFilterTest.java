//
//  ActionRegistryFilterTest.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.console.actions;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.TestCaseEx;
import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.console.ActionRegistry;
import spacemadness.com.lunarconsole.console.ActionRegistryFilter;
import spacemadness.com.lunarconsole.console.IdentityEntry;
import spacemadness.com.lunarconsole.console.Variable;
import spacemadness.com.lunarconsole.console.VariableType;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;
import static spacemadness.com.lunarconsole.utils.StringUtils.*;

public class ActionRegistryFilterTest extends TestCaseEx implements ActionRegistryFilter.Delegate
{
    private int _nextActionId;
    private ActionRegistry _actionRegistry;
    private ActionRegistryFilter _registryFilter;

    // MARK: - Setup

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        _actionRegistry = new ActionRegistry();
        _registryFilter = new ActionRegistryFilter(_actionRegistry);
        _registryFilter.setDelegate(this);
        _nextActionId = 0;
    }

    // MARK: - Filter by text

    public void testFilterByText()
    {
        registerEntries(
                new LUActionInfo("line1"),
                new LUActionInfo("line11"),
                new LUActionInfo("line111"),
                new LUActionInfo("line1111"),
                new LUActionInfo("foo"),
                new LUCVarInfo("line1", "value", VariableType.String),
                new LUCVarInfo("line11", "value", VariableType.String),
                new LUCVarInfo("line111", "value", VariableType.String),
                new LUCVarInfo("line1111", "value", VariableType.String),
                new LUCVarInfo("foo", "value", VariableType.String)
        );

        assertNotFiltering();

        assertTrue(setFilter("l"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(!setFilter("l"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("li"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("lin"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line1"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line11"));
        assertActions("line11", "line111", "line1111");
        assertVariables("line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line111"));
        assertActions("line111", "line1111");
        assertVariables("line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line1111"));
        assertActions("line1111");
        assertVariables("line1111");
        assertFiltering();

        assertTrue(setFilter("line11111"));
        assertNoActions();
        assertFiltering();

        assertTrue(setFilter("line1111"));
        assertActions("line1111");
        assertVariables("line1111");
        assertFiltering();

        assertTrue(setFilter("line111"));
        assertActions("line111", "line1111");
        assertVariables("line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line11"));
        assertActions("line11", "line111", "line1111");
        assertVariables("line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line1"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("line"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("lin"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("li"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter("l"));
        assertActions("line1", "line11", "line111", "line1111");
        assertVariables("line1", "line11", "line111", "line1111");
        assertFiltering();

        assertTrue(setFilter(""));
        assertActions("foo", "line1", "line11", "line111", "line1111");
        assertVariables("foo", "line1", "line11", "line111", "line1111");
        assertNotFiltering();
    }

    // MARK: - Register entries

    public void testRegisterEntries()
    {
        registerAction("a11");
        registerVariable("v11");

        registerAction("a1");
        registerVariable("v1");

        registerAction("a111");
        registerVariable("v111");

        assertActions("a1", "a11", "a111");
        assertVariables("v1", "v11", "v111");
    }

    public void testRegisterEntriesFiltered()
    {
        setFilter("a11");

        registerAction("a11");
        registerAction("a1");
        registerAction("a111");

        assertActions("a11", "a111");
        assertNoVariables();

        registerVariable("v11");
        registerVariable("v1");
        registerVariable("v111");

        setFilter("v11");

        assertNoActions();
        assertVariables("v11", "v111");

        // remove the filter
        setFilter("");

        assertActions("a1", "a11", "a111");
        assertVariables("v1", "v11", "v111");
    }

    public void testRegisterMultipleActionsWithSameName()
    {
        registerAction("a2");
        registerAction("a3");
        registerAction("a1");
        registerAction("a3");

        assertActions("a1", "a2", "a3");
    }

    public void testRegisterMultipleActionsWithSameNameFiltered()
    {
        setFilter("a1");
        assertFiltering();

        registerAction("a");
        registerAction("a11");
        registerAction("a12");
        registerAction("a11");

        assertActions("a11", "a12");

        setFilter("a");
        assertFiltering();
        assertActions("a", "a11", "a12");

        setFilter("");
        assertNotFiltering();
        assertActions("a", "a11", "a12");
    }

    // MARK: - Unregister actions

    public void testUnregisterActions()
    {
        int id2 = registerAction("a2").actionId();
        int id1 = registerAction("a1").actionId();
        int id3 = registerAction("a3").actionId();

        unregisterAction(id1);
        assertActions("a2", "a3");

        unregisterAction(id2);
        assertActions("a3");

        unregisterAction(id3);
        assertNoActions();

        unregisterAction(id3);
        assertNoActions();
    }

    public void testUnregisterActionFiltered()
    {
        setFilter("a11");

        int id2 = registerAction("a11").actionId();
        int id1 = registerAction("a1").actionId();
        int id3 = registerAction("a111").actionId();

        unregisterAction(id1);
        assertActions("a11", "a111");

        unregisterAction(id2);
        assertActions("a111");

        unregisterAction(id3);
        assertNoActions();

        unregisterAction(id3);
        assertNoActions();
    }

    // MARK: - Delegate notifications

    public void testDelegateNotifications()
    {
        // register actions
        registerAction("a2");

        assertResult("added action: a2 (0)");

        registerAction("a1");
        assertResult("added action: a1 (0)");

        registerAction("a3");
        assertResult("added action: a3 (2)");

        // register variables
        registerVariable("1.bool", VariableType.Boolean, "1");
        assertResult("register variable: Boolean 1.bool 1 (0)");

        registerVariable("2.int", VariableType.Integer, "10");
        assertResult("register variable: Integer 2.int 10 (1)");

        registerVariable("3.float", VariableType.Float, "3.14");
        assertResult("register variable: Float 3.float 3.14 (2)");

        registerVariable("4.string", VariableType.String, "value");
        assertResult("register variable: String 4.string value (3)");

        // unregister variables
        unregisterAction("a1");
        assertResult("removed action: a1 (0)");

        unregisterAction("a3");
        assertResult("removed action: a3 (1)");

        unregisterAction("a2");
        assertResult("removed action: a2 (0)");
    }

    public void testDelegateNotificationsFiltered()
    {
        // set filter
        setFilter("a1");

        // register actions
        registerAction("a");

        registerAction("a11");
        assertResult("added action: a11 (0)");

        registerAction("a12");
        assertResult("added action: a12 (1)");

        // register variables
        registerVariable("a", VariableType.Boolean, "1");

        registerVariable("a1", VariableType.Integer, "10");
        assertResult("register variable: Integer a1 10 (0)");

        registerVariable("a12", VariableType.Float, "3.14");
        assertResult("register variable: Float a12 3.14 (1)");

        registerVariable("a13", VariableType.String, "value");
        assertResult("register variable: String a13 value (2)");

        // unregister variables
        unregisterAction("a1");

        unregisterAction("a11");
        assertResult("removed action: a11 (0)");

        unregisterAction("a12");
        assertResult("removed action: a12 (0)");
    }

    public void testFilteringByTextAddActions()
    {
        assertNotFiltering();

        assertTrue(setFilter("line11"));
        registerAction(0, "line111");

        assertActions("line111");
        assertFiltering();

        registerAction(1, "line1");
        registerAction(2, "line11");

        assertActions("line11", "line111");

        unregisterAction(2);
        assertActions("line111");

        unregisterAction(1);
        assertActions("line111");

        unregisterAction(0);
        assertNoActions();

        registerAction(3, "line1");
        assertNoActions();

        registerAction(4, "line11");
        assertActions("line11");

        setFilter("");
        assertActions("line1", "line11");
    }

    // MARK: - LUActionRegistryFilterDelegate

    @Override
    public void actionRegistryFilterDidAddAction(ActionRegistryFilter registryFilter, Action action, int index)
    {
        addResult("added action: %s (%d)", action.name(), index);
    }

    @Override
    public void actionRegistryFilterDidRemoveAction(ActionRegistryFilter registryFilter, Action action, int index)
    {
        addResult("removed action: %s (%d)", action.name(), index);
    }

    @Override
    public void actionRegistryFilterDidRegisterVariable(ActionRegistryFilter registryFilter, Variable variable, int index)
    {
        addResult("register variable: %s %s %s (%d)", variable.type, variable.name(), variable.value, index);
    }

    @Override
    public void actionRegistryFilterDidChangeVariable(ActionRegistryFilter registryFilter, Variable variable, int index)
    {
        fail("Implement me");
    }

    // MARK: - Helpers

    private boolean setFilter(String text)
    {
        return _registryFilter.setFilterText(text);
    }

    private Action registerAction(String name)
    {
        _nextActionId = _nextActionId + 1;
        return registerAction(_nextActionId, name);
    }

    private Action registerAction(int id, String name)
    {
        return _actionRegistry.registerAction(id, name);
    }

    private Variable registerVariable(String name, VariableType type, String value)
    {
        _nextActionId = _nextActionId + 1;
        return _actionRegistry.registerVariable(_nextActionId, name, type, value, value);
    }

    private Variable registerVariable(String name)
    {
        _nextActionId = _nextActionId + 1;
        return _actionRegistry.registerVariable(_nextActionId, name, VariableType.String, "value", "value");
    }

    private boolean unregisterAction(String name)
    {
        for (int i = 0; i < _actionRegistry.actions().size(); ++i)
        {
            Action action = as(_actionRegistry.actions().get(i), Action.class);
            if (action != null && action.name().equals(name))
            {
                _actionRegistry.unregisterAction(action.actionId());
                return true;
            }
        }

        return false;
    }

    private void unregisterAction(int id)
    {
        _actionRegistry.unregisterAction(id);
    }

    private void registerEntries(LUEntryInfo... entries)
    {

        for (LUEntryInfo info : entries)
        {
            _nextActionId = _nextActionId + 1;

            if (info instanceof LUActionInfo)
            {
                _actionRegistry.registerAction(_nextActionId, info.name);
            } else if (info instanceof LUCVarInfo)
            {
                LUCVarInfo cvar = (LUCVarInfo) info;
                _actionRegistry.registerVariable(_nextActionId, cvar.name, cvar.type, cvar.value, cvar.value);
            } else
            {
                fail("Unexpected entry: " + info);
            }
        }
    }

    // MARK: - Assertion Helpers

    private void assertNoActions()
    {
        assertTrue(_registryFilter.actions().size() == 0);
    }

    private void assertNoVariables()
    {
        assertTrue(_registryFilter.variables().size() == 0);
    }

    private void assertActions(String... names)
    {
        List<String> actualNames = new ArrayList<>();
        for (int i = 0; i < _registryFilter.actions().size(); ++i)
        {
            Action action = as(_registryFilter.actions().get(i), Action.class);
            actualNames.add(action.name());
        }

        String message = String.format("Expected %s but was %s", Join(names, ","), Join(actualNames, ","));
        assertEquals(message, names.length, actualNames.size());

        for (int i = 0; i < actualNames.size(); ++i)
        {
            assertEquals(message, names[i], actualNames.get(i));
        }
    }

    private void assertVariables(String... names)
    {
        List<String> actualNames = new ArrayList<>();
        for (int i = 0; i < _registryFilter.variables().size(); ++i)
        {
            Variable variable = as(_registryFilter.variables().get(i), Variable.class);
            actualNames.add(variable.name());
        }

        String message = String.format("Expected %s but was %s", Join(names, ","), Join(actualNames, ","));
        assertEquals(message, names.length, actualNames.size());

        for (int i = 0; i < actualNames.size(); ++i)
        {
            assertEquals(message, names[i], actualNames.get(i));
        }
    }

    private void assertFiltering()
    {
        assertTrue(_registryFilter.isFiltering());
    }

    private void assertNotFiltering()
    {
        assertFalse(_registryFilter.isFiltering());
    }

    private static class LUEntryInfo
    {
        final String name;

        LUEntryInfo(String name)
        {
            this.name = name;
        }

        public boolean isEqual(IdentityEntry entry)
        {
            return false;
        }
    }

    private static class LUActionInfo extends LUEntryInfo
    {
        LUActionInfo(String name)
        {
            super(name);
        }

        @Override
        public boolean isEqual(IdentityEntry entry)
        {
            Action action = as(entry, Action.class);
            return action != null && action.name().equals(name);
        }
    }

    private static class LUCVarInfo extends LUEntryInfo
    {
        public final String value;
        public final VariableType type;

        LUCVarInfo(String name, String value, VariableType type)
        {
            super(name);
            this.value = value;
            this.type = type;
        }

        @Override
        public boolean isEqual(IdentityEntry entry)
        {
            Variable cvar = as(entry, Variable.class);
            return cvar != null &&
                    this.name.equals(cvar.name()) &&
                    this.value.equals(cvar.value) &&
                    this.type.equals(cvar.type);

        }
    }
}
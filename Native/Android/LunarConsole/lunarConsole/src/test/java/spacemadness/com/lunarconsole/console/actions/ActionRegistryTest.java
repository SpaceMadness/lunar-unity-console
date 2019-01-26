//
//  ActionRegistryTest.java
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

import spacemadness.com.lunarconsole.TestCaseEx;
import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.console.ActionRegistry;
import spacemadness.com.lunarconsole.console.Variable;
import spacemadness.com.lunarconsole.console.VariableType;

public class ActionRegistryTest extends TestCaseEx implements ActionRegistry.Delegate
{
    private ActionRegistry actionRegistry;
    private int nextActionId;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        actionRegistry = new ActionRegistry();
        actionRegistry.setDelegate(this);
        nextActionId = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Register

    public void testRegisterActionsAndVariables()
    {
        registerActionWithName("a2");
        registerActionWithName("a1");
        registerActionWithName("a3");

        registerVariableWithName("v2");
        registerVariableWithName("v1");
        registerVariableWithName("v3");

        assertActions("a1", "a2", "a3");
        assertVariables("v1", "v2", "v3");
    }

    public void testRegisterMultipleActionsWithSameName()
    {
        registerActionWithName("a2");
        registerActionWithName("a3");
        registerActionWithName("a1");
        registerActionWithName("a3");

        assertActions("a1", "a2", "a3");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Unregister actions

    public void testUnregisterAction()
    {
        int id2 = registerActionWithName("a2").actionId();
        int id1 = registerActionWithName("a1").actionId();
        int id3 = registerActionWithName("a3").actionId();

        unregisterActionWithId(id1);
        assertActions("a2", "a3");

        unregisterActionWithId(id2);
        assertActions("a3");

        unregisterActionWithId(id3);
        assertActions();

        unregisterActionWithId(id3);
        assertActions();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Delegate notifications

    public void testDelegateNotifications()
    {
        // register actions
        registerActionWithName("a2");
        assertResult("added action: a2 (0)");

        registerActionWithName("a1");
        assertResult("added action: a1 (0)");

        registerActionWithName("a3");
        assertResult("added action: a3 (2)");

        // register variables
        registerVariableWithName("1.bool", VariableType.Boolean, "1");
        assertResult("register variable: Boolean 1.bool 1 (0)");

        registerVariableWithName("2.int", VariableType.Integer, "10");
        assertResult("register variable: Integer 2.int 10 (1)");

        registerVariableWithName("3.float", VariableType.Float, "3.14");
        assertResult("register variable: Float 3.float 3.14 (2)");

        registerVariableWithName("4.string", VariableType.String, "value");
        assertResult("register variable: String 4.string value (3)");

        // unregister variables
        unregisterActionWithName("a1");
        assertResult("removed action: a1 (0)");

        unregisterActionWithName("a3");
        assertResult("removed action: a3 (1)");

        unregisterActionWithName("a2");
        assertResult("removed action: a2 (0)");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Delegate

    @Override
    public void didAddAction(ActionRegistry registry, Action action, int index)
    {
        addResult(String.format("added action: %s (%d)", action.name(), index));
    }

    @Override
    public void didRemoveAction(ActionRegistry registry, Action action, int index)
    {
        addResult(String.format("removed action: %s (%d)", action.name(), index));
    }

    @Override
    public void didRegisterVariable(ActionRegistry registry, Variable variable, int index)
    {
        addResult(String.format("register variable: %s %s %s (%d)", variable.type, variable.name(), variable.value, index));
    }

    @Override
    public void didDidChangeVariable(ActionRegistry registry, Variable variable, int index)
    {
        fail("Implement me");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private Action registerActionWithName(String name)
    {
        return actionRegistry.registerAction(nextActionId++, name);
    }

    private Variable registerVariableWithName(String name)
    {
        return registerVariableWithName(name, VariableType.String);
    }

    private Variable registerVariableWithName(String name, VariableType type)
    {
        return registerVariableWithName(name, type, "value");
    }

    private Variable registerVariableWithName(String name, VariableType type, String value)
    {
        return actionRegistry.registerVariable(nextActionId++, name, type, value, value);
    }

    private void unregisterActionWithId(int actionId)
    {
        actionRegistry.unregisterAction(actionId);
    }

    private boolean unregisterActionWithName(String name)
    {
        for (Action action : actionRegistry.actions())
        {
            if (action.name().equals(name))
            {
                actionRegistry.unregisterAction(action.actionId());
                return true;
            }
        }

        return false;
    }

    private void assertActions(String... expected)
    {
        assertEquals(expected.length, actionRegistry.actions().size());

        int index = 0;
        for (Action action : actionRegistry.actions())
        {
            assertEquals(expected[index], action.name());
            ++index;
        }
    }

    private void assertVariables(String... expected)
    {
        assertEquals(expected.length, actionRegistry.variables().size());

        int index = 0;
        for (Variable cvar : actionRegistry.variables())
        {
            assertEquals(expected[index], cvar.name());
            ++index;
        }
    }

    @Override
    protected void assertResult(String... expected)
    {
        super.assertResult(expected);
        clearResult();
    }
}
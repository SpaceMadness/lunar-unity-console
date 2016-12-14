package spacemadness.com.lunarconsole.console.actions;

import spacemadness.com.lunarconsole.TestCaseEx;
import static spacemadness.com.lunarconsole.console.actions.LUCVar.*;

public class LUActionRegistryTest extends TestCaseEx implements LUActionRegistry.Delegate
{
    private LUActionRegistry _actionRegistry;
    private int _nextActionId;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        _actionRegistry = new LUActionRegistry();
        _actionRegistry.setDelegate(this);
        _nextActionId = 0;
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
        registerVariableWithName("1.bool", LUCVarTypeNameBoolean, "1");
        assertResult("register variable: Boolean 1.bool 1 (0)");

        registerVariableWithName("2.int", LUCVarTypeNameInteger, "10");
        assertResult("register variable: Integer 2.int 10 (1)");

        registerVariableWithName("3.float", LUCVarTypeNameFloat, "3.14");
        assertResult("register variable: Float 3.float 3.14 (2)");

        registerVariableWithName("4.string", LUCVarTypeNameString, "value");
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
    // LUActionRegistryDelegte

    @Override
    public void didAddAction(LUActionRegistry registry, LUAction action)
    {
        addResult(String.format("added action: %s", action.name()));
    }

    @Override
    public void didRemoveAction(LUActionRegistry registry, LUAction action)
    {
        addResult(String.format("removed action: %s", action.name()));
    }

    @Override
    public void didRegisterVariable(LUActionRegistry registry, LUCVar variable)
    {
        addResult(String.format("register variable: %s %s %s", LUCVar typeNameForType:variable.type, variable.name(), variable.value));
    }

    @Override
    public void didDidChangeVariable(LUActionRegistry registry, LUCVar variable)
    {
        fail("Implement me");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private LUAction registerActionWithName(String name)
    {
        return _actionRegistry.registerActionWithId(_nextActionId++, name);
    }

    private LUCVar registerVariableWithName(String name)
    {
        return registerVariableWithName(name, LUCVarTypeNameString);
    }

    private LUCVar registerVariableWithName(String name, String typeName)
    {
        return registerVariableWithName(name, typeName, "value");
    }

    private LUCVar registerVariableWithName(String name, String typeName, String value)
    {
        return _actionRegistry.registerVariableWithId(_nextActionId++, name, typeName, value);
    }

    private void unregisterActionWithId(int actionId)
    {
        _actionRegistry.unregisterActionWithId(actionId);
    }

    private boolean unregisterActionWithName(String name)
    {
        for (LUAction action : _actionRegistry.actions())
        {
            if (action.name().equals(name))
            {
                _actionRegistry.unregisterActionWithId(action.actionId());
                return true;
            }
        }

        return false;
    }

    private void assertActions(String... expected)
    {
        assertEquals(expected.length, _actionRegistry.actions().count);

        int index = 0;
        for (LUAction action : _actionRegistry.actions)
        {
            assertEquals(expected[index], action.name());
            ++index;
        }
    }

    private void assertVariables(String... expected)
    {
        assertEquals(expected.length, _actionRegistry.variables().count);

        int index = 0;
        for (LUCVar cvar : _actionRegistry.variables)
        {
            assertEquals(expected[index], cvar.name());
            ++index;
        }
    }
}
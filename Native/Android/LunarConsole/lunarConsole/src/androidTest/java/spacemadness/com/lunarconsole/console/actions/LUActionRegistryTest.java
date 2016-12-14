package spacemadness.com.lunarconsole.console.actions;

import java.util.List;

import spacemadness.com.lunarconsole.TestCaseEx;

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
        registerVariableWithName("1.bool", LUCVarType.Boolean, "1");
        assertResult("register variable: Boolean 1.bool 1 (0)");

        registerVariableWithName("2.int", LUCVarType.Integer, "10");
        assertResult("register variable: Integer 2.int 10 (1)");

        registerVariableWithName("3.float", LUCVarType.Float, "3.14");
        assertResult("register variable: Float 3.float 3.14 (2)");

        registerVariableWithName("4.string", LUCVarType.String, "value");
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
    public void didAddAction(LUActionRegistry registry, LUAction action, int index)
    {
        addResult(String.format("added action: %s (%d)", action.name(), index));
    }

    @Override
    public void didRemoveAction(LUActionRegistry registry, LUAction action, int index)
    {
        addResult(String.format("removed action: %s (%d)", action.name(), index));
    }

    @Override
    public void didRegisterVariable(LUActionRegistry registry, LUCVar variable, int index)
    {
        addResult(String.format("register variable: %s %s %s (%d)", variable.type(), variable.name(), variable.value(), index));
    }

    @Override
    public void didDidChangeVariable(LUActionRegistry registry, LUCVar variable, int index)
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
        return registerVariableWithName(name, LUCVarType.String);
    }

    private LUCVar registerVariableWithName(String name, LUCVarType type)
    {
        return registerVariableWithName(name, type, "value");
    }

    private LUCVar registerVariableWithName(String name, LUCVarType type, String value)
    {
        return _actionRegistry.registerVariableWithId(_nextActionId++, name, type, value, value);
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
        assertEquals(expected.length, _actionRegistry.actions().count());

        int index = 0;
        for (LUAction action : _actionRegistry.actions())
        {
            assertEquals(expected[index], action.name());
            ++index;
        }
    }

    private void assertVariables(String... expected)
    {
        assertEquals(expected.length, _actionRegistry.variables().count());

        int index = 0;
        for (LUCVar cvar : _actionRegistry.variables())
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
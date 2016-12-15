package spacemadness.com.lunarconsole.console.actions;

import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.LUSortedList;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

/**
 * Created by alementuev on 12/13/16.
 */

public class LUActionRegistry // FIXME: rename
{
    private final LUSortedList<LUAction> _actions; // FIXME: rename
    private final LUSortedList<LUCVar> _variables; // FIXME: rename
    private Delegate _delegate; // FIXME: rename

    public LUActionRegistry()
    {
        _actions = new LUSortedList<>();
        _variables = new LUSortedList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Actions

    public LUAction registerActionWithId(int actionId, String actionName) // FIXME: rename
    {
        int actionIndex = indexOfActionWithName(actionName);
        if (actionIndex == -1)
        {
            LUAction action = new LUAction(actionId, actionName);
            actionIndex = _actions.addObject(action);
            _delegate.didAddAction(this, action, actionIndex);
        }

        return _actions.objectAtIndex(actionIndex);
    }

    public boolean unregisterActionWithId(int actionId) // FIXME: rename
    {
        for (int actionIndex = _actions.count() - 1; actionIndex >= 0; --actionIndex)
        {
            LUAction action = _actions.objectAtIndex(actionIndex);
            if (action.actionId() == actionId)
            {
                _actions.removeObjectAtIndex(actionIndex);
                _delegate.didRemoveAction(this, action, actionIndex);

                return true;
            }
        }

        return false;
    }

    private int indexOfActionWithName(String actionName) // FIXME: rename
    {
        // TODO: more optimized search
        for (int index = 0; index < _actions.count(); ++index)
        {
            LUAction action = _actions.objectAtIndex(index);
            if (ObjectUtils.areEqual(action.name(), actionName))
            {
                return index;
            }
        }

        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Variables

    public LUCVar registerVariableWithId(int variableId, String name, LUCVarType type, String value, String defaultValue) // FIXME: rename
    {
        LUCVar variable = new LUCVar(variableId, name, value, defaultValue, type);
        int index = _variables.addObject(variable);
        _delegate.didRegisterVariable(this, variable, index);

        return variable;

    }

    public void setValue(String value, int variableId) // FIXME: rename
    {
        int index = indexOfVariableWithId(variableId);
        if (index != -1)
        {
            LUCVar cvar = _variables.objectAtIndex(index);
            cvar.setValue(value);
            _delegate.didDidChangeVariable(this, cvar, index);
        }
        else
        {
            Log.e("Can't server cvar value: variable id %d not found", variableId);
        }
    }

    public LUCVar variableWithId(int variableId) // FIXME: rename
    {
        int index = indexOfVariableWithId(variableId);
        return index != -1 ? _variables.objectAtIndex(index) : null;
    }

    private int indexOfVariableWithId(int variableId) // FIXME: rename
    {
        int index = 0;
        for (LUCVar cvar : _variables)
        {
            if (cvar.actionId() == variableId)
            {
                return index;
            }

            ++index;
        }

        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public List<LUAction> actions() // FIXME: rename
    {
        return _actions.list();
    }

    public List<LUCVar> variables() // FIXME: rename
    {
        return _variables.list();
    }

    public Delegate getDelegate()
    {
        return _delegate;
    }

    public void setDelegate(Delegate delegate)
    {
        this._delegate = delegate;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Delegate

    public interface Delegate // FIXME: rename
    {
        void didAddAction(LUActionRegistry registry, LUAction action, int index);
        void didRemoveAction(LUActionRegistry registry, LUAction action, int index);
        void didRegisterVariable(LUActionRegistry registry, LUCVar variable, int index);
        void didDidChangeVariable(LUActionRegistry registry, LUCVar variable, int index);
    }
}

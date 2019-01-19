//
//  ActionRegistry.java
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

package spacemadness.com.lunarconsole.console;

import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.LUSortedList;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

public class ActionRegistry
{
    private final LUSortedList<Action> actions;
    private final LUSortedList<Variable> variables;
    private Delegate delegate;

    public ActionRegistry()
    {
        actions = new LUSortedList<>();
        variables = new LUSortedList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Actions

    public Action registerAction(int actionId, String actionName)
    {
        int actionIndex = indexOfAction(actionName);
        if (actionIndex == -1)
        {
            Action action = new Action(actionId, actionName);
            actionIndex = actions.addObject(action);
            notifyActionAdd(action, actionIndex);
        }

        return actions.objectAtIndex(actionIndex);
    }

    public boolean unregisterAction(int actionId)
    {
        for (int actionIndex = actions.count() - 1; actionIndex >= 0; --actionIndex)
        {
            Action action = actions.objectAtIndex(actionIndex);
            if (action.actionId() == actionId)
            {
                actions.removeObjectAtIndex(actionIndex);
                notifyActionRemove(action, actionIndex);

                return true;
            }
        }

        return false;
    }

    private int indexOfAction(String actionName)
    {
        // TODO: more optimized search
        for (int index = 0; index < actions.count(); ++index)
        {
            Action action = actions.objectAtIndex(index);
            if (ObjectUtils.areEqual(action.name(), actionName))
            {
                return index;
            }
        }

        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Variables

    public Variable registerVariable(int variableId, String name, VariableType type, String value, String defaultValue) // FIXME: rename
    {
        Variable variable = new Variable(variableId, name, value, defaultValue, type);
        int index = variables.addObject(variable);
        notifyVariableRegister(variable, index);

        return variable;

    }

    public void updateVariable(int variableId, String value)
    {
        int index = indexOfVariable(variableId);
        if (index != -1)
        {
            Variable cvar = variables.objectAtIndex(index);
            cvar.value = value;
            notifyVariableChange(cvar, index);
        }
        else
        {
            Log.e("Can't server cvar value: variable id %d not found", variableId);
        }
    }

    public Variable findVariable(int variableId)
    {
        int index = indexOfVariable(variableId);
        return index != -1 ? variables.objectAtIndex(index) : null;
    }

    private int indexOfVariable(int variableId) // FIXME: rename
    {
        int index = 0;
        for (Variable cvar : variables)
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
    // Delegate notifications

    private void notifyActionAdd(Action action, int actionIndex)
    {
        if (delegate != null)
        {
            delegate.didAddAction(this, action, actionIndex);
        }
    }

    private void notifyActionRemove(Action action, int actionIndex)
    {
        if (delegate != null)
        {
            delegate.didRemoveAction(this, action, actionIndex);
        }
    }

    private void notifyVariableRegister(Variable variable, int index)
    {
        if (delegate != null)
        {
            delegate.didRegisterVariable(this, variable, index);
        }
    }

    private void notifyVariableChange(Variable cvar, int index)
    {
        if (delegate != null)
        {
            delegate.didDidChangeVariable(this, cvar, index);
        }
    }

    //region Getters/Setters

    public List<Action> actions() // FIXME: rename
    {
        return actions.list();
    }

    public List<Variable> variables() // FIXME: rename
    {
        return variables.list();
    }

    public Delegate getDelegate()
    {
        return delegate;
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    public void setActionSortingEnabled(boolean sortingEnabled)
    {
        actions.setSortingEnabled(sortingEnabled);
    }

    public void setVariableSortingEnabled(boolean sortingEnabled)
    {
        variables.setSortingEnabled(sortingEnabled);
    }

    //endregion

    //region Delegate

    public interface Delegate // FIXME: rename
    {
        void didAddAction(ActionRegistry registry, Action action, int index);
        void didRemoveAction(ActionRegistry registry, Action action, int index);
        void didRegisterVariable(ActionRegistry registry, Variable variable, int index);
        void didDidChangeVariable(ActionRegistry registry, Variable variable, int index);
    }

    //endregion
}

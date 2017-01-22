//
//  LUActionRegistry.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

import java.util.List;

import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.LUSortedList;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

public class LUActionRegistry // FIXME: rename
{
    private final LUSortedList<LUAction> actions;
    private final LUSortedList<LUCVar> variables;
    private Delegate delegate;

    public LUActionRegistry()
    {
        actions = new LUSortedList<>();
        variables = new LUSortedList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Actions

    public LUAction registerAction(int actionId, String actionName)
    {
        int actionIndex = indexOfAction(actionName);
        if (actionIndex == -1)
        {
            LUAction action = new LUAction(actionId, actionName);
            actionIndex = actions.addObject(action);
            notifyActionAdd(action, actionIndex);
        }

        return actions.objectAtIndex(actionIndex);
    }

    public boolean unregisterAction(int actionId)
    {
        for (int actionIndex = actions.count() - 1; actionIndex >= 0; --actionIndex)
        {
            LUAction action = actions.objectAtIndex(actionIndex);
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
            LUAction action = actions.objectAtIndex(index);
            if (ObjectUtils.areEqual(action.name(), actionName))
            {
                return index;
            }
        }

        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Variables

    public LUCVar registerVariable(int variableId, String name, LUCVarType type, String value, String defaultValue) // FIXME: rename
    {
        LUCVar variable = new LUCVar(variableId, name, value, defaultValue, type);
        int index = variables.addObject(variable);
        notifyVariableRegister(variable, index);

        return variable;

    }

    public void setVariableValue(String value, int variableId)
    {
        int index = indexOfVariable(variableId);
        if (index != -1)
        {
            LUCVar cvar = variables.objectAtIndex(index);
            cvar.setValue(value);
            notifyVariableChange(cvar, index);
        }
        else
        {
            Log.e("Can't server cvar value: variable id %d not found", variableId);
        }
    }

    public LUCVar findVariable(int variableId)
    {
        int index = indexOfVariable(variableId);
        return index != -1 ? variables.objectAtIndex(index) : null;
    }

    private int indexOfVariable(int variableId) // FIXME: rename
    {
        int index = 0;
        for (LUCVar cvar : variables)
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

    private void notifyActionAdd(LUAction action, int actionIndex)
    {
        if (delegate != null)
        {
            delegate.didAddAction(this, action, actionIndex);
        }
    }

    private void notifyActionRemove(LUAction action, int actionIndex)
    {
        if (delegate != null)
        {
            delegate.didRemoveAction(this, action, actionIndex);
        }
    }

    private void notifyVariableRegister(LUCVar variable, int index)
    {
        if (delegate != null)
        {
            delegate.didRegisterVariable(this, variable, index);
        }
    }

    private void notifyVariableChange(LUCVar cvar, int index)
    {
        if (delegate != null)
        {
            delegate.didDidChangeVariable(this, cvar, index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public List<LUAction> actions() // FIXME: rename
    {
        return actions.list();
    }

    public List<LUCVar> variables() // FIXME: rename
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

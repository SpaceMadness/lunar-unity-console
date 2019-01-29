//
//  ActionRegistryFilter.java
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

import java.util.ArrayList;
import java.util.List;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;
import static spacemadness.com.lunarconsole.utils.StringUtils.*;

public class ActionRegistryFilter implements ActionRegistry.RegistryListener
{
    private final ActionRegistry registry;
    private String filterText;
    private List<Action> filteredActions;
    private List<Variable> filteredVariables;
    private Delegate delegate;

    public ActionRegistryFilter(ActionRegistry actionRegistry)
    {
        registry = actionRegistry;
        registry.setRegistryListener(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Filtering

    public boolean setFilterText(String filterText)
    {
        if (!areEqual(this.filterText, filterText)) // filter text has changed
        {
            String oldFilterText = this.filterText;
            this.filterText = filterText;

            if (length(filterText) > length(oldFilterText) && (length(oldFilterText) == 0 ||  hasPrefix(filterText, oldFilterText))) // added more characters
            {
                return appendFilter();
            }

            return applyFilter();
        }

        return false;
    }

    /** applies filter to already filtered items */
    private boolean appendFilter()
    {
        if (isFiltering())
        {
            filteredActions = filterEntries(filteredActions);
            filteredVariables = filterEntries(filteredVariables);
            return true;
        }

        return applyFilter();
    }

    /** setup filtering for the list */
    private boolean applyFilter()
    {
        if (length(filterText) > 0)
        {
            filteredActions = filterEntries(getAllActions());
            filteredVariables = filterEntries(getAllVariables());
            return true;
        }

        return removeFilter();
    }

    private boolean removeFilter()
    {
        if (isFiltering())
        {
            filteredActions = null;
            filteredVariables = null;
            return true;
        }

        return false;
    }

    private <T extends IdentityEntry> List<T> filterEntries(List<T> entries)
    {
        List<T> filteredEntries = new ArrayList<>();
        for (T entry : entries)
        {
            if (filterEntry(entry))
            {
                filteredEntries.add(entry);
            }
        }

        return filteredEntries;
    }

    private boolean filterEntry(IdentityEntry entry)
    {
        return length(filterText) == 0 || containsIgnoreCase(entry.getName(), filterText);
    }

    private <T extends IdentityEntry> int filteredArrayAddEntry(List<T> array, T entry)
    {
        // insert in the sorted order
        for (int index = 0; index < array.size(); ++index)
        {
            int comparisonResult = entry.compareTo(array.get(index));
            if (comparisonResult < 0)
            {
                array.add(index, entry);
                return index;
            }
            else if (comparisonResult == 0)
            {
                return index; // filtered group exists
            }
        }

        array.add(entry);
        return array.size() - 1;
    }

    private int filteredArrayIndexOfEntry(List<? extends IdentityEntry> array, IdentityEntry entry)
    {
        for (int index = 0; index < array.size(); ++index)
        {
            IdentityEntry existing = array.get(index);
            if (existing.actionId() == entry.actionId())
            {
                return index;
            }
        }

        return -1;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LUActionRegistryDelegate

    @Override
    public void onAddAction(ActionRegistry registry, Action action, int index)
    {
        if (isFiltering())
        {
            if (!filterEntry(action))
            {
                return;
            }

            index = filteredArrayAddEntry(filteredActions, action);
        }

        delegate.actionRegistryFilterDidAddAction(this, action, index);
    }

    @Override
    public void onRemoveAction(ActionRegistry registry, Action action, int index)
    {
        if (isFiltering())
        {
            index = filteredArrayIndexOfEntry(filteredActions, action);
            if (index == -1)
            {
                return;
            }

            action = filteredActions.get(index);
            filteredActions.remove(index);
        }

        delegate.actionRegistryFilterDidRemoveAction(this, action, index);
    }

    @Override
    public void onRegisterVariable(ActionRegistry registry, Variable variable, int index)
    {
        if (isFiltering())
        {
            if (!filterEntry(variable))
            {
                return;
            }

            index = filteredArrayAddEntry(filteredVariables, variable);
        }

        delegate.actionRegistryFilterDidRegisterVariable(this, variable, index);
    }

    @Override
    public void onChangeVariable(ActionRegistry registry, Variable variable, int index)
    {
        if (isFiltering())
        {
            if (!filterEntry(variable))
            {
                return;
            }

            index = filteredArrayIndexOfEntry(filteredVariables, variable);
        }

        delegate.actionRegistryFilterDidChangeVariable(this, variable, index);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    public List<Action> actions()
    {
        return isFiltering() ? filteredActions : getAllActions();
    }

    public List<Action> getAllActions()
    {
        return registry.actions();
    }

    public List<Variable> variables()
    {
        return isFiltering() ? filteredVariables : getAllVariables();
    }

    public List<Variable> getAllVariables()
    {
        return registry.variables();
    }

    public boolean isFiltering()
    {
        return filteredActions != null || filteredVariables != null;
    }

    public String getFilterText()
    {
        return filterText;
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
    // TODO: proper name

    public interface Delegate
    {
        void actionRegistryFilterDidAddAction(ActionRegistryFilter registryFilter, Action action, int index);
        void actionRegistryFilterDidRemoveAction(ActionRegistryFilter registryFilter, Action action, int index);
        void actionRegistryFilterDidRegisterVariable(ActionRegistryFilter registryFilter, Variable variable, int index);
        void actionRegistryFilterDidChangeVariable(ActionRegistryFilter registryFilter, Variable variable, int index);
    }
}

//
//  LUActionRegistryFilter.java
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

import java.util.ArrayList;
import java.util.List;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;
import static spacemadness.com.lunarconsole.utils.StringUtils.*;

public class LUActionRegistryFilter implements LUActionRegistry.Delegate // FIXME: rename
{
    private final LUActionRegistry _registry;
    private String _filterText;
    private List<LUAction> _filteredActions;
    private List<LUCVar> _filteredVariables;
    private Delegate _delegate;

    public LUActionRegistryFilter(LUActionRegistry actionRegistry)
    {
        _registry = actionRegistry;
        _registry.setDelegate(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Filtering

    public boolean setFilterText(String filterText)
    {
        if (!areEqual(_filterText, filterText)) // filter text has changed
        {
            String oldFilterText = _filterText;
            _filterText = filterText;

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
            _filteredActions = filterEntries(_filteredActions);
            _filteredVariables = filterEntries(_filteredVariables);
            return true;
        }

        return applyFilter();
    }

    /** setup filtering for the list */
    private boolean applyFilter()
    {
        if (length(_filterText) > 0)
        {
            _filteredActions = filterEntries(getAllActions());
            _filteredVariables = filterEntries(getAllVariables());
            return true;
        }

        return removeFilter();
    }

    private boolean removeFilter()
    {
        if (isFiltering())
        {
            _filteredActions = null;
            _filteredVariables = null;
            return true;
        }

        return false;
    }

    private <T extends BaseIdentityEntry> List<T> filterEntries(List<T> entries)
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

    private boolean filterEntry(BaseIdentityEntry entry)
    {
        return length(_filterText) == 0 || contains(entry.name(), _filterText);
    }

    private <T extends BaseIdentityEntry> int filteredArrayAddEntry(List<T> array, T entry)
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

    private int filteredArrayIndexOfEntry(List<? extends BaseIdentityEntry> array, BaseIdentityEntry entry)
    {
        for (int index = 0; index < array.size(); ++index)
        {
            BaseIdentityEntry existing = array.get(index);
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
    public void didAddAction(LUActionRegistry registry, LUAction action, int index)
    {
        if (isFiltering())
        {
            if (!filterEntry(action))
            {
                return;
            }

            index = filteredArrayAddEntry(_filteredActions, action);
        }

        _delegate.actionRegistryFilterDidAddAction(this, action, index);
    }

    @Override
    public void didRemoveAction(LUActionRegistry registry, LUAction action, int index)
    {
        if (isFiltering())
        {
            index = filteredArrayIndexOfEntry(_filteredActions, action);
            if (index == -1)
            {
                return;
            }

            action = _filteredActions.get(index);
            _filteredActions.remove(index);
        }

        _delegate.actionRegistryFilterDidRemoveAction(this, action, index);
    }

    @Override
    public void didRegisterVariable(LUActionRegistry registry, LUCVar variable, int index)
    {
        if (isFiltering())
        {
            if (!filterEntry(variable))
            {
                return;
            }

            index = filteredArrayAddEntry(_filteredVariables, variable);
        }

        _delegate.actionRegistryFilterDidRegisterVariable(this, variable, index);
    }

    @Override
    public void didDidChangeVariable(LUActionRegistry registry, LUCVar variable, int index)
    {
        if (isFiltering())
        {
            if (!filterEntry(variable))
            {
                return;
            }

            index = filteredArrayIndexOfEntry(_filteredVariables, variable);
        }

        _delegate.actionRegistryFilterDidChangeVariable(this, variable, index);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    public List<LUAction> actions()
    {
        return isFiltering() ? _filteredActions : getAllActions();
    }

    public List<LUAction> getAllActions()
    {
        return _registry.actions();
    }

    public List<LUCVar> variables()
    {
        return isFiltering() ? _filteredVariables : getAllVariables();
    }

    public List<LUCVar> getAllVariables()
    {
        return _registry.variables();
    }

    public boolean isFiltering()
    {
        return _filteredActions != null || _filteredVariables != null;
    }

    public String getFilterText()
    {
        return _filterText;
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
    // TODO: proper name

    public interface Delegate
    {
        void actionRegistryFilterDidAddAction(LUActionRegistryFilter registryFilter, LUAction action, int index);
        void actionRegistryFilterDidRemoveAction(LUActionRegistryFilter registryFilter, LUAction action, int index);
        void actionRegistryFilterDidRegisterVariable(LUActionRegistryFilter registryFilter, LUCVar variable, int index);
        void actionRegistryFilterDidChangeVariable(LUActionRegistryFilter registryFilter, LUCVar variable, int index);
    }
}

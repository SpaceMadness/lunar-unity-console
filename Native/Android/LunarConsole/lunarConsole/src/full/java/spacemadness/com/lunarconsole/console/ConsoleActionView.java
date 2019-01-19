//
//  ConsoleActionView.java
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

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.ui.ConsoleListView;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.ThreadUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static spacemadness.com.lunarconsole.console.BaseConsoleAdapter.DataSource;
import static spacemadness.com.lunarconsole.console.Notifications.*;
import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;

public class ConsoleActionView extends AbstractConsoleView implements
        ActionRegistryFilter.Delegate, Destroyable
{
    private final View contentView; // contains the whole view hierarchy
    private final View warningView; // contains "no actions" warning view

    private final ActionRegistryFilter registryFilter;
    private final ConsoleActionAdapter consoleActionAdapter;

    private final ConsoleViewState consoleViewState;

    public ConsoleActionView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        contentView = findViewById(R.id.lunar_console_actions_view);
        warningView = findViewById(R.id.lunar_console_actions_warning_view);

        consoleViewState = consolePlugin.getConsoleViewState();

        registryFilter = new ActionRegistryFilter(consolePlugin.getActionRegistry());
        registryFilter.setFilterText(consoleViewState.getActionFilterText());
        registryFilter.setDelegate(this);

        // initialize adapter
        final ActionDataSource dataSource = new ActionDataSource(getContext(), registryFilter);
        consoleActionAdapter = new ConsoleActionAdapter(dataSource);

        // this view would hold all the logs
        LinearLayout listViewContainer = findExistingViewById(R.id.lunar_console_action_view_list_container);

        ListView listView = new ConsoleListView(activity);
        listView.setAdapter(consoleActionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id)
            {
                final Context ctx = getContext();
                final Action action = as(dataSource.getEntry(position), Action.class);
                if (action == null)
                {
                    return;
                }

                // post notification
                NotificationCenter.defaultCenter().postNotification(NOTIFICATION_ACTION_SELECT, NOTIFICATION_KEY_ACTION, action);

                // visual feedback
                // TODO: user color resource and animation
                view.setBackgroundColor(0xff000000);
                ThreadUtils.runOnUIThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            view.setBackgroundColor(action.getBackgroundColor(ctx, position));
                        }
                        catch (Exception e)
                        {
                            Log.e(e, "Error while settings entry background color");
                        }
                    }
                }, 200);
            }
        });

        listViewContainer.addView(listView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

        // filtering
        setupFilterTextEdit();

        // fetch some data
        reloadData();

        // update "no actions" warning
        updateNoActionWarningView();

        // "help" button
        setOnClickListener(R.id.lunar_console_no_actions_button_help, new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UIUtils.openURL(getContext(), "https://goo.gl/in0obv");
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI elements

    private EditText setupFilterTextEdit()
    {
        // TODO: make a custom class
        EditText editText = findExistingViewById(R.id.lunar_console_action_view_text_edit_filter);
        String filterText = registryFilter.getFilterText();
        if (!StringUtils.IsNullOrEmpty(filterText))
        {
            editText.setText(filterText);
            editText.setSelection(filterText.length());
        }

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String filterText = s.toString();
                filterByText(filterText);
                consoleViewState.setActionFilterText(filterText);
                updateNoActionWarningView();
            }
        });

        return editText;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Filtering

    private void filterByText(String filterText)
    {
        boolean changed = registryFilter.setFilterText(filterText);
        if (changed)
        {
            reloadData();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // No actions warning

    private void updateNoActionWarningView()
    {
        boolean hasContent = registryFilter.getAllActions().size() > 0 ||
                             registryFilter.getAllVariables().size() > 0;
        setNoActionsWarningViewHidden(hasContent);
    }

    private void setNoActionsWarningViewHidden(boolean hidden)
    {
        warningView.setVisibility(hidden ? GONE : VISIBLE);
        contentView.setVisibility(hidden ? VISIBLE : GONE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data

    private void reloadData()
    {
        consoleActionAdapter.notifyDataSetChanged();
    }

    private void notifyDataChanged()
    {
        consoleActionAdapter.notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        // TODO: destroy stuff
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ActionRegistryFilter.Delegate

    @Override
    public void actionRegistryFilterDidAddAction(ActionRegistryFilter registryFilter, Action action, int index)
    {
        notifyDataChanged();
        updateNoActionWarningView();
    }

    @Override
    public void actionRegistryFilterDidRemoveAction(ActionRegistryFilter registryFilter, Action action, int index)
    {
        notifyDataChanged();
        updateNoActionWarningView();
    }

    @Override
    public void actionRegistryFilterDidRegisterVariable(ActionRegistryFilter registryFilter, Variable variable, int index)
    {
        notifyDataChanged();
        updateNoActionWarningView();
    }

    @Override
    public void actionRegistryFilterDidChangeVariable(ActionRegistryFilter registryFilter, Variable variable, int index)
    {
        notifyDataChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data Source

    private static class ActionDataSource implements DataSource<IdentityEntry>
    {
        private final ActionRegistryFilter actionRegistryFilter;
        private final IdentityEntry actionsHeader;
        private final IdentityEntry variablesHeader;

        private ActionDataSource(Context context, ActionRegistryFilter actionRegistryFilter)
        {
            this.actionRegistryFilter = actionRegistryFilter;
            actionsHeader = new HeaderEntry(context.getString(R.string.lunar_console_header_actions));
            variablesHeader = new HeaderEntry(context.getString(R.string.lunar_console_header_variables));
        }

        @Override
        public IdentityEntry getEntry(int position)
        {
            List<Action> actions = getActions();
            if (actions.size() > 0)
            {
                if (position == 0) return actionsHeader;

                int actionIndex = position - 1;
                if (actionIndex < actions.size())
                {
                    return actions.get(actionIndex);
                }

                position -= actions.size() + 1;
            }

            return position == 0 ? variablesHeader : getVariables().get(position - 1);
        }

        @Override
        public int getEntryCount()
        {
            int count = 0;

            List<Action> actions = getActions();
            count += actions.size() > 0 ? (actions.size() + 1) : 0;

            List<Variable> variables = getVariables();
            count += variables.size() > 0 ? (variables.size() + 1) : 0;

            return count;
        }

        private List<Action> getActions()
        {
            return actionRegistryFilter.actions();
        }
        private List<Variable> getVariables()
        {
            return actionRegistryFilter.variables();
        }
    }
}

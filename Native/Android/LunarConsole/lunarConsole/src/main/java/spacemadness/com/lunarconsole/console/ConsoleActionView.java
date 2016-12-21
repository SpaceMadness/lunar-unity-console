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
import spacemadness.com.lunarconsole.console.actions.LUAction;
import spacemadness.com.lunarconsole.console.actions.LUActionRegistry;
import spacemadness.com.lunarconsole.console.actions.LUActionRegistryFilter;
import spacemadness.com.lunarconsole.console.actions.LUCVar;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.ui.ConsoleListView;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static spacemadness.com.lunarconsole.console.BaseConsoleAdapter.DataSource;
import static spacemadness.com.lunarconsole.console.ConsoleNotifications.*;

public class ConsoleActionView extends AbstractConsoleView implements
        LUActionRegistryFilter.Delegate, Destroyable
{
    private final LUActionRegistryFilter registryFilter;
    private final ListView listView;
    private final ConsoleActionAdapter consoleActionAdapter;

    public ConsoleActionView(Activity activity, LUActionRegistry actionRegistry)
    {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        registryFilter = new LUActionRegistryFilter(actionRegistry);
        registryFilter.setDelegate(this);

        // initialize adapter
        final ActionDataSource dataSource = new ActionDataSource(registryFilter);
        consoleActionAdapter = new ConsoleActionAdapter(dataSource);

        // this view would hold all the logs
        LinearLayout listViewContainer = findExistingViewById(R.id.lunar_console_list_view_container);

        listView = new ConsoleListView(activity);
        listView.setAdapter(consoleActionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id)
            {
                final Context ctx = getContext();
                final LUAction action = dataSource.getEntry(position);

                // post notification
                NotificationCenter.defaultCenter().postNotification(ACTION_SELECT, ACTION_SELECT_KEY_ACTION, action);

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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI elements

    private EditText setupFilterTextEdit()
    {
        // TODO: make a custom class
        EditText editText = findExistingViewById(R.id.lunar_console_text_edit_filter);
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
                filterByText(s.toString());
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
    // LUActionRegistryFilter.Delegate

    @Override
    public void actionRegistryFilterDidAddAction(LUActionRegistryFilter registryFilter, LUAction action, int index)
    {
        notifyDataChanged();
    }

    @Override
    public void actionRegistryFilterDidRemoveAction(LUActionRegistryFilter registryFilter, LUAction action, int index)
    {
        notifyDataChanged();
    }

    @Override
    public void actionRegistryFilterDidRegisterVariable(LUActionRegistryFilter registryFilter, LUCVar variable, int index)
    {
        notifyDataChanged();
    }

    @Override
    public void actionRegistryFilterDidChangeVariable(LUActionRegistryFilter registryFilter, LUCVar variable, int index)
    {
        notifyDataChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data Source

    private static class ActionDataSource implements DataSource<LUAction>
    {
        private final LUActionRegistryFilter actionRegistryFilter;

        private ActionDataSource(LUActionRegistryFilter actionRegistryFilter)
        {
            this.actionRegistryFilter = actionRegistryFilter;
        }

        @Override
        public LUAction getEntry(int position)
        {
            return getActions().get(position);
        }

        @Override
        public int getEntryCount()
        {
            return getActions().size();
        }

        private List<LUAction> getActions()
        {
            return actionRegistryFilter.actions();
        }
    }
}

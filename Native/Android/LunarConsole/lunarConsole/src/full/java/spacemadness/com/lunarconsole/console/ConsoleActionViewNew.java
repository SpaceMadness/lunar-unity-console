//
//  ConsoleActionView.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.rx.Observer;
import spacemadness.com.lunarconsole.ui.ConsoleListView;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.ListViewAdapter.LayoutIdFactory;
import spacemadness.com.lunarconsole.ui.ListViewAdapter.ViewHolder;
import spacemadness.com.lunarconsole.ui.ListViewItem;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ConsoleActionViewNew extends AbstractConsoleView implements Destroyable {
    private final View contentView; // contains the whole view hierarchy
    private final View warningView; // contains "no actions" warning view

    private final ListViewAdapter listAdapter;
    private final ConsoleViewState consoleViewState;

    public ConsoleActionViewNew(Activity activity, final ConsoleActionViewModel viewModel) {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        contentView = findViewById(R.id.lunar_console_actions_view);
        warningView = findViewById(R.id.lunar_console_actions_warning_view);

        consoleViewState = viewModel.getConsoleViewState();

        // initialize adapter
        listAdapter = createListViewAdapter();

        // this view would hold all the logs
        LinearLayout listViewContainer = findExistingViewById(R.id.lunar_console_action_view_list_container);

        ListView listView = new ConsoleListView(activity);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//                final Context ctx = getContext();
//                final Action action = as(dataSource.getEntry(position), Action.class);
//                if (action == null) {
//                    return;
//                }
//
//                // post notification
//                NotificationCenter.defaultCenter().postNotification(NOTIFICATION_ACTION_SELECT, NOTIFICATION_KEY_ACTION, action);
//
//                // visual feedback
//                // TODO: user color resource and animation
//                view.setBackgroundColor(0xff000000);
//
//                dispatchQueue.dispatch(new DispatchTask() {
//                    @Override
//                    protected void execute() {
//                        try {
//                            view.setBackgroundColor(action.getBackgroundColor(ctx, position));
//                        } catch (Exception e) {
//                            Log.e(e, "Error while settings entry background color");
//                        }
//                    }
//                }, 200);
            }
        });

        listViewContainer.addView(listView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

        registerSubscription(
                viewModel.getItemsStream().subscribe(new Observer<List<ListViewItem>>() {
                    @Override
                    public void onChanged(List<ListViewItem> items) {
                        listAdapter.submitList(items);
                    }
                }));

        // "help" button
        setOnClickListener(R.id.lunar_console_no_actions_button_help, new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.openHelp();
            }
        });
    }

    private ListViewAdapter createListViewAdapter() {
        return new ListViewAdapter()
                // header
                .register(EntryType.Header, new LayoutIdFactory(R.layout.lunar_console_layout_console_header_entry) {
                    @Override
                    public ViewHolder<?> createViewHolder(View convertView) {
                        return new HeaderListItem.ViewHolder(convertView);
                    }
                })
                // action
                .register(EntryType.Action, new LayoutIdFactory(R.layout.lunar_console_layout_console_action_entry) {
                    @Override
                    public ViewHolder<?> createViewHolder(View convertView) {
                        return new ActionListItem.ViewHolder(convertView);
                    }
                })
                // variable
                .register(EntryType.Variable, new LayoutIdFactory(R.layout.lunar_console_layout_console_variable_entry) {
                    @Override
                    public ViewHolder<?> createViewHolder(View convertView) {
                        return new VariableListItem.ViewHolder(convertView);
                    }
                });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI elements

    private EditText setupFilterTextEdit() {
        // TODO: make a custom class
        EditText editText = findExistingViewById(R.id.lunar_console_action_view_text_edit_filter);
//        String filterText = registryFilter.getFilterText();
//        if (!StringUtils.IsNullOrEmpty(filterText)) {
//            editText.setText(filterText);
//            editText.setSelection(filterText.length());
//        }
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String filterText = s.toString();
//                filterByText(filterText);
//                consoleViewState.setActionFilterText(filterText);
//                updateNoActionWarningView();
//            }
//        });

        return editText;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // No actions warning

    private void setNoActionsWarningViewHidden(boolean hidden) {
        warningView.setVisibility(hidden ? GONE : VISIBLE);
        contentView.setVisibility(hidden ? VISIBLE : GONE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data

    private void reloadData() {
        listAdapter.notifyDataSetChanged();
    }
}

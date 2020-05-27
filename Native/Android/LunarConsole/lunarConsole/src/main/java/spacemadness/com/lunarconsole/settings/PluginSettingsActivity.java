//
//  PluginSettingsActivity.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2020 Alex Lementuev, SpaceMadness.
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

package spacemadness.com.lunarconsole.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.dependency.PluginSettingsEditorProvider;
import spacemadness.com.lunarconsole.dependency.Provider;
import spacemadness.com.lunarconsole.settings.PluginSettingsViewModel.HeaderItem;
import spacemadness.com.lunarconsole.settings.PluginSettingsViewModel.PropertyItem;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.CollectionUtils;
import spacemadness.com.lunarconsole.utils.EnumUtils;
import spacemadness.com.lunarconsole.utils.NotImplementedException;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

import static spacemadness.com.lunarconsole.settings.PluginSettingsViewModel.ItemType;

public class PluginSettingsActivity extends Activity {
	private ListView listView;
	private ListViewAdapter adapter;

	private PluginSettingsEditor settingsEditor;

	private PluginSettingsViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lunar_console_layout_activity_settings);

		settingsEditor = Provider.of(PluginSettingsEditorProvider.class).getSettingsEditor();

		viewModel = new PluginSettingsViewModel(settingsEditor);

		adapter = createAdapter(viewModel);

		listView = findViewById(R.id.lunar_console_settings_list_view);
		listView.setDivider(null);
		listView.setAdapter(adapter);
	}

	private ListViewAdapter createAdapter(PluginSettingsViewModel viewModel) {
		List<ListViewItem> items = viewModel.createItems();
		ListViewAdapter adapter = new ListViewAdapter(items);

		// Header
		adapter.register(ItemType.HEADER, new ListViewAdapter.LayoutIdFactory(R.layout.lunar_console_layout_settings_header) {
			@Override
			public ListViewAdapter.ViewHolder createViewHolder(View convertView) {
				return new HeaderViewHolder(convertView);
			}
		});

		// Property
		adapter.register(ItemType.PROPERTY, new ListViewAdapter.LayoutIdFactory(R.layout.lunar_console_layout_settings_property) {
			@Override
			public ListViewAdapter.ViewHolder createViewHolder(View convertView) {
				return new PropertyViewHolder(convertView);
			}
		});

		return adapter;
	}

	private static class HeaderViewHolder extends ListViewAdapter.ViewHolder<HeaderItem> {
		private final TextView headerTitleTextView;

		protected HeaderViewHolder(View view) {
			super(view);
			headerTitleTextView = view.findViewById(R.id.lunar_console_settings_header);
		}

		@Override
		protected void bindView(HeaderItem item, int position) {
			headerTitleTextView.setText(item.title);
		}
	}

	private class PropertyViewHolder extends ListViewAdapter.ViewHolder<PropertyItem> {
		private final TextView titleTextView;
		private final EditText valueEditText;
		private final Switch valueSwitch;
		private final Button valueButton;
		private final ImageButton lockButton;

		public PropertyViewHolder(View view) {
			super(view);

			titleTextView = view.findViewById(R.id.lunar_console_settings_property_name);
			valueEditText = view.findViewById(R.id.lunar_console_settings_property_value);
			valueSwitch = view.findViewById(R.id.lunar_console_settings_property_switch);
			valueButton = view.findViewById(R.id.lunar_console_settings_property_button);
			lockButton = view.findViewById(R.id.lunar_console_settings_property_lock_button);
		}

		@Override
		public void bindView(final PropertyItem item, int position) {
			titleTextView.setText(item.displayName);

			lockButton.setVisibility(item.enabled ? View.GONE : View.VISIBLE);
			lockButton.setOnClickListener(item.enabled ? null : createLockClickListener(lockButton.getContext()));

			int valueEditTextVisibility = View.GONE;
			int valueSwitchVisibility = View.GONE;
			int valueButtonVisibility = View.GONE;

			final Class<?> type = item.getType();
			final Object value = item.getValue();
			if (type == Boolean.class || type == boolean.class) {
				valueSwitchVisibility = View.VISIBLE;
				valueSwitch.setChecked(value == Boolean.TRUE);
				valueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						item.setValue(isChecked);
					}
				});
				valueSwitch.setEnabled(item.enabled);
			} else {
				final String valueStr = StringUtils.toString(value);
				if (type.isEnum()) {
					valueButtonVisibility = View.VISIBLE;
					valueButton.setText(valueStr);
					valueButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							showEnumDialog(v.getContext(), item.displayName, value, new EnumDialogCallback() {
								@Override
								public void onValueSelected(Object value) {
									item.setValue(value);
									valueButton.setText(value.toString());
								}
							});
						}
					});
					valueButton.setEnabled(item.enabled);
				} else {
					valueEditTextVisibility = View.VISIBLE;
					valueEditText.setEnabled(item.enabled);

					/* This helps avoiding a weird flickering while keyboard is shown */
					if (!valueStr.equals(valueEditText.getText().toString())) {
						valueEditText.setText(valueStr);
						if (value instanceof Number) {
							int inputType = InputType.TYPE_CLASS_NUMBER;
							if (value instanceof Float || value instanceof Double) {
								inputType |= InputType.TYPE_NUMBER_FLAG_DECIMAL;
							}
							valueEditText.setInputType(inputType);
						}
						valueEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
							@Override
							public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
								if (actionId == EditorInfo.IME_ACTION_DONE) {
									Object newValue = parseItemValue(v.getText().toString(), type);
									if (newValue != null) {
										item.setValue(newValue);
									}
								}
								return false;
							}

							private Object parseItemValue(String value, Class<?> type) {
								if (type == Integer.class || type == int.class) {
									return StringUtils.parseInt(value);
								}
								if (type == Float.class || type == float.class) {
									return StringUtils.parseFloat(value);
								}

								throw new NotImplementedException("Unsupported type: " + type);
							}
						});
					}
				}
			}

			valueEditText.setVisibility(valueEditTextVisibility);
			valueSwitch.setVisibility(valueSwitchVisibility);
			valueButton.setVisibility(valueButtonVisibility);
		}

		private void showEnumDialog(Context context, String name, Object value, final EnumDialogCallback callback) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(name);
			builder.setCancelable(true);

			final Object[] values = EnumUtils.listValues((Enum<?>) value);
			int checkItem = CollectionUtils.indexOf(values, value);
			String[] names = CollectionUtils.map(values, new CollectionUtils.Map<Object, String>() {
				@Override public String map(Object o) {
					return StringUtils.toString(o);
				}
			});

			builder.setSingleChoiceItems(names, checkItem, null);

			builder.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						ListView lw = ((AlertDialog) dialog).getListView();
						Object newValue = values[lw.getCheckedItemPosition()];
						callback.onValueSelected(newValue);
						dialog.cancel();
					}
				});

			builder.setNegativeButton(
				android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			builder.create().show();
		}

		private View.OnClickListener createLockClickListener(final Context context) {
			return new View.OnClickListener() {
				@Override public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(R.string.lunar_console_settings_lock_dialog_title);
					builder.setCancelable(true);

					builder.setPositiveButton(
						R.string.lunar_console_settings_lock_dialog_learn_more,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								UIUtils.openURL(context, context.getString(R.string.lunar_console_settings_lock_dialog_learn_more_url));
							}
						});

					builder.setNegativeButton(
						android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

					builder.create().show();
				}
			};
		}
	}

	private interface EnumDialogCallback {
		void onValueSelected(Object value);
	}
}
package spacemadness.com.lunarconsole.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import spacemadness.com.lunarconsole.utils.StringUtils;

import static spacemadness.com.lunarconsole.settings.PluginSettingsViewModel.ItemType;

public class PluginSettingsActivity extends Activity {
	private ListView listView;
	private ListViewAdapter adapter;

	private PluginSettings settings;
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

		public PropertyViewHolder(View view) {
			super(view);

			titleTextView = view.findViewById(R.id.lunar_console_settings_property_name);
			valueEditText = view.findViewById(R.id.lunar_console_settings_property_value);
			valueSwitch = view.findViewById(R.id.lunar_console_settings_property_switch);
			valueButton = view.findViewById(R.id.lunar_console_settings_property_button);
		}

		@Override
		public void bindView(final PropertyItem item, int position) {
			titleTextView.setText(item.displayName);

			int valueEditTextVisibility = View.GONE;
			int valueSwitchVisibility = View.GONE;
			int valueButtonVisibility = View.GONE;

			Class<?> type = item.getType();
			if (type == Boolean.class || type == boolean.class) {
				valueSwitchVisibility = View.VISIBLE;
				valueSwitch.setChecked(item.getValue() == Boolean.TRUE);
				valueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						item.setValue(isChecked);
					}
				});
			} else if (type.isEnum()) {
				valueButtonVisibility = View.VISIBLE;
				valueButton.setText(StringUtils.toString(item.getValue()));
				valueButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						showEnumDialog(v.getContext(), item);
					}
				});
			} else {
				valueEditTextVisibility = View.VISIBLE;
				valueEditText.setText(StringUtils.toString(item.getValue()));
			}

			valueEditText.setVisibility(valueEditTextVisibility);
			valueSwitch.setVisibility(valueSwitchVisibility);
			valueButton.setVisibility(valueButtonVisibility);
		}

		private void showEnumDialog(Context context, final PropertyItem item) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(item.displayName);
			builder.setCancelable(true);

			final Object[] values = EnumUtils.listValues((Enum<?>) item.getValue());
			int checkItem = CollectionUtils.indexOf(values, item.getValue());
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
						item.setValue(values[lw.getCheckedItemPosition()]);
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
	}
}
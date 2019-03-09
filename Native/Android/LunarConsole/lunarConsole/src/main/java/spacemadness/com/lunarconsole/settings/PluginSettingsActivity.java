package spacemadness.com.lunarconsole.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.dependency.Provider;
import spacemadness.com.lunarconsole.reflection.FieldProperty;
import spacemadness.com.lunarconsole.reflection.Property;
import spacemadness.com.lunarconsole.reflection.PropertyHelper;
import spacemadness.com.lunarconsole.settings.PluginSettingsViewModel.HeaderItem;
import spacemadness.com.lunarconsole.settings.PluginSettingsViewModel.PropertyItem;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.ClassUtils;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

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

		settingsEditor = Provider.of(PluginSettingsEditor.class);

		viewModel = new PluginSettingsViewModel(settingsEditor);

		adapter = createAdapter(viewModel);

		listView = findViewById(R.id.lunar_console_settings_list_view);
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
//		adapter.register(ItemType.PROPERTY, new ListViewAdapter.LayoutIdFactory() {
//			@Override
//			public ListViewAdapter.ViewHolder createViewHolder(View convertView) {
//				throw new NotImplementedException();
//			}
//		});

		return adapter;
	}

	private List<ListViewItem> createItems(PluginSettings settings) {
		return viewModel.createItems();
	}

	private void notifyPropertyChanged(FieldProperty property) {
		throw new NotImplementedException();
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
		public PropertyViewHolder(View view) {
			super(view);
		}

		@Override
		public void bindView(PropertyItem item, int position) {
			throw new NotImplementedException();
		}
	}
}

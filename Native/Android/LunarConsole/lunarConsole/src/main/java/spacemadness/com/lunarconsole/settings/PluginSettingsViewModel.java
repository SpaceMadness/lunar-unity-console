package spacemadness.com.lunarconsole.settings;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.reflection.FieldProperty;
import spacemadness.com.lunarconsole.reflection.PropertyHelper;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.StringUtils;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

class PluginSettingsViewModel {
	private final PluginSettingsEditor settingsEditor;

	PluginSettingsViewModel(PluginSettingsEditor settingsEditor) {
		this.settingsEditor = checkNotNull(settingsEditor, "settingsEditor");
	}

	List<ListViewItem> createItems() {
		PluginSettings settings = settingsEditor.getSettings();

		List<ListViewItem> items = new ArrayList<>();
		items.add(new HeaderItem("Exception Warning"));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "exceptionWarning.displayMode")));
		items.add(new HeaderItem("Log Overlay"));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay.enabled")));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay.maxVisibleLines")));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay.timeout")));
		return items;
	}

	private void notifyPropertyChanged(FieldProperty property) {
		settingsEditor.setSettings(settingsEditor.getSettings());
	}

	enum ItemType {HEADER, PROPERTY}

	static abstract class Item extends ListViewItem {
		private final ItemType type;

		Item(ItemType type) {
			this.type = type;
		}

		@Override
		protected int getItemViewType() {
			return type.ordinal();
		}
	}

	static class HeaderItem extends Item {
		public final String title;

		HeaderItem(String title) {
			super(ItemType.HEADER);
			this.title = title;
		}
	}

	class PropertyItem extends Item {
		private final FieldProperty property;
		final String displayName;

		PropertyItem(FieldProperty property) {
			super(ItemType.PROPERTY);
			this.property = property;
			this.displayName = StringUtils.camelCaseToWords(property.name);
		}

		public Object getValue() {
			return property.getValue();
		}

		public void setValue(Object value) {
			property.setValue(value);
			notifyPropertyChanged(property);
		}

		public Class<?> getType() {
			return property.getType();
		}
	}
}

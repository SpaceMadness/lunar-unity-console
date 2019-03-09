package spacemadness.com.lunarconsole.settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.reflection.FieldProperty;
import spacemadness.com.lunarconsole.reflection.Property;
import spacemadness.com.lunarconsole.reflection.PropertyHelper;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.ClassUtils;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

class PluginSettingsViewModel {
	private final PluginSettingsEditor settingsEditor;

	public PluginSettingsViewModel(PluginSettingsEditor settingsEditor) {
		this.settingsEditor = checkNotNull(settingsEditor, "settingsEditor");
	}

	public List<ListViewItem> createItems() {
		PluginSettings settings = settingsEditor.getSettings();

		List<ListViewItem> items = new ArrayList<>();
		items.add(new HeaderItem("Exception Warning"));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "exceptionWarning/displayMode")));
		items.add(new HeaderItem("Log Overlay"));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay/enabled")));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay/maxVisibleLines")));
		items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay/timeout")));
		return items;
	}

	private void notifyPropertyChanged(FieldProperty property) {
	}

	enum ItemType {HEADER, PROPERTY}

	static abstract class Item extends ListViewItem {
		private final ItemType type;

		protected Item(ItemType type) {
			this.type = type;
		}

		@Override
		protected int getItemViewType() {
			return type.ordinal();
		}
	}

	static class HeaderItem extends Item {
		public final String title;

		public HeaderItem(String title) {
			super(ItemType.HEADER);
			this.title = title;
		}
	}

	class PropertyItem extends Item {
		public final FieldProperty property;

		public PropertyItem(FieldProperty property) {
			super(ItemType.PROPERTY);
			this.property = property;
		}

		public void setValue(Object value) {
			property.setValue(value);
			notifyPropertyChanged(property);
		}
	}
}

package spacemadness.com.lunarconsole.settings;

import java.util.List;

import spacemadness.com.lunarconsole.InstrumentationTestCase;
import spacemadness.com.lunarconsole.json.JsonDecoder;
import spacemadness.com.lunarconsole.ui.ListViewItem;

public class PluginSettingsViewModelTest extends InstrumentationTestCase {

	public void testCreateItems() {
		final PluginSettingsEditor settingsEditor = new PluginSettingsEditor() {
			@Override
			public PluginSettings getSettings() {
				return readSettings();
			}

			@Override
			public void setSettings(PluginSettings settings) {

			}
		};

		PluginSettingsViewModel viewModel = new PluginSettingsViewModel(settingsEditor);
		List<ListViewItem> items = viewModel.createItems();
		System.out.println(items);
	}

	private PluginSettings readSettings() {
		String settingsJson = readTextAsset("editor-settings.json");
		return JsonDecoder.decode(settingsJson, PluginSettings.class);
	}
}
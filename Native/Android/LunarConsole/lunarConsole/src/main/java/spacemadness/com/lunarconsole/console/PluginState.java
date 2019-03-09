package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.redux.State;
import spacemadness.com.lunarconsole.settings.PluginSettings;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class PluginState implements State {
	private PluginSettings settings;
	private boolean consoleOpened;

	public PluginState(PluginSettings settings) {
		this.settings = checkNotNull(settings, "settings");
	}

	public PluginSettings getSettings() {
		return settings;
	}

	public boolean isConsoleOpened() {
		return consoleOpened;
	}
}

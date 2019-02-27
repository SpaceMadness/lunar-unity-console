package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.redux.State;

public class PluginState implements State {
	public final boolean consoleOpened;

	public PluginState(boolean consoleOpened) {
		this.consoleOpened = consoleOpened;
	}
}

package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.redux.Action;
import spacemadness.com.lunarconsole.redux.Reducer;

class ConsolePluginReducer implements Reducer<PluginState> {

	@Override public PluginState reduce(PluginState state, Action action) {
		return state;
	}
}

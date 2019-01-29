package spacemadness.com.lunarconsole.model;

import java.util.List;

import spacemadness.com.lunarconsole.console.Action;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class ActionGroup {
	private final String name;
	private final List<Action> actions;
	private final boolean collapsed;

	public ActionGroup(String name, List<Action> actions, boolean collapsed) {
		this.name = checkNotNull(name, "name");
		this.actions = checkNotNull(actions, "actions");
		this.collapsed = collapsed;
	}

	public String getName() {
		return name;
	}

	public List<Action> getActions() {
		return actions;
	}

	public boolean isCollapsed() {
		return collapsed;
	}
}

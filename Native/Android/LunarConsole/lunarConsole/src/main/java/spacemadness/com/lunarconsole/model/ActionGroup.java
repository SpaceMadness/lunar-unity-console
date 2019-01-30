package spacemadness.com.lunarconsole.model;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class ActionGroup {
	private final String name;
	private final List<Action> actions;
	private boolean collapsed;

	public ActionGroup(String name) {
		this.name = checkNotNull(name, "name");
		this.actions = new ArrayList<>();
	}

	public void addAction(Action action) {
		if (!ObjectUtils.areEqual(action.getGroupName(), name)) {
			throw new IllegalArgumentException("Invalid group name for action '"
				                                   + action.getFullName() + "'. Expected '" + getName() + "'.");
		}
		actions.add(action);
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

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
}

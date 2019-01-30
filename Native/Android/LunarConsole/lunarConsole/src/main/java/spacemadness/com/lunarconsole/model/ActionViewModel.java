package spacemadness.com.lunarconsole.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.core.LiveData;
import spacemadness.com.lunarconsole.core.MutableLiveData;
import spacemadness.com.lunarconsole.utils.Collections;
import spacemadness.com.lunarconsole.utils.NotImplementedException;
import spacemadness.com.lunarconsole.utils.StringUtils;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.toBoolean;

public class ActionViewModel {
	private final MutableLiveData<List<ActionGroup>> actionGroups;
	private final Map<String, Boolean> collapsedGroupLookup;
	private List<Action> cachedActions;
	private String filterText;
	private boolean shouldSortActions;
	private boolean shouldSortVariables;

	public ActionViewModel() {
		this(DispatchQueue.mainQueue());
	}

	public ActionViewModel(DispatchQueue dispatchQueue) {
		actionGroups = new MutableLiveData<>(Collections.<ActionGroup>emptyList(), dispatchQueue);
		collapsedGroupLookup = new HashMap<>();
	}

	public void setActions(List<Action> actions) {
		cachedActions = actions;
		actionGroups.setValue(getActionGroups(actions));
	}

	private void updateActions() {
		setActions(cachedActions);
	}

	private List<ActionGroup> getActionGroups(List<Action> actions) {
		List<ActionGroup> groups = new ArrayList<>();
		Map<String, ActionGroup> groupLookup = new HashMap<>();
		for (Action action : actions) {
			if (!filter(action)) {
				continue;
			}

			String groupName = action.getGroupName();
			ActionGroup group = groupLookup.get(groupName);
			if (group == null) {
				group = new ActionGroup(groupName);
				group.setCollapsed(isGroupCollapsed(groupName));
				groups.add(group);
				groupLookup.put(groupName, group);
			}
			group.addAction(action);
		}
		return groups;
	}

	//region Groups

	public boolean isGroupCollapsed(String groupName) {
		return toBoolean(collapsedGroupLookup.get(groupName), false);
	}

	public void setGroupCollapsed(String groupName, boolean collapsed) {
		// collapsedGroupLookup.put(checkNotNull(groupName, "groupName"), collapsed);
		throw new NotImplementedException();
	}

	//endregion

	//region Filtering

	private boolean filter(Action action) {
		return !isFiltering() ||
			       StringUtils.filter(action.getName(), filterText) ||
			       StringUtils.filter(action.getGroupName(), filterText);
	}

	//endregion

	//region Properties

	public LiveData<List<ActionGroup>> getActionGroups() {
		return actionGroups;
	}

	public boolean isFiltering() {
		return !StringUtils.IsNullOrEmpty(filterText);
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}

	//endregion
}

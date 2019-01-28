package spacemadness.com.lunarconsole.data;

import java.util.List;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.core.LiveData;
import spacemadness.com.lunarconsole.core.MutableLiveData;
import spacemadness.com.lunarconsole.utils.ObjectUtils;
import spacemadness.com.lunarconsole.utils.SortedList;

class ConsoleActionRepository {
	private final SortedList<Action> actions;
	private final MutableLiveData<List<Action>> data;

	ConsoleActionRepository() {
		this(DispatchQueue.mainQueue());
	}

	ConsoleActionRepository(DispatchQueue dispatchQueue) {
		actions = new SortedList<>();
		data = new MutableLiveData<>(actions.asList(), dispatchQueue);
	}

	public Action addAction(int actionId, String actionName) {
		int actionIndex = indexOf(actionName);
		if (actionIndex == -1) {
			Action action = new Action(actionId, actionName);
			actions.addObject(action);
			notifyObservers();
			return action;
		}

		return actions.objectAtIndex(actionIndex);
	}

	public boolean removeAction(int actionId) {
		for (int actionIndex = actions.count() - 1; actionIndex >= 0; --actionIndex) {
			Action action = actions.objectAtIndex(actionIndex);
			if (action.actionId() == actionId) {
				actions.removeObjectAtIndex(actionIndex);
				notifyObservers();

				return true;
			}
		}

		return false;
	}

	private void notifyObservers() {
		data.setValue(actions.asList());
	}

	private int indexOf(String name) {
		for (int index = 0; index < actions.count(); ++index) {
			Action action = actions.objectAtIndex(index);
			if (ObjectUtils.areEqual(action.name(), name)) {
				return index;
			}
		}

		return -1;
	}

	public LiveData<List<Action>> getActions() {
		return data;
	}
}

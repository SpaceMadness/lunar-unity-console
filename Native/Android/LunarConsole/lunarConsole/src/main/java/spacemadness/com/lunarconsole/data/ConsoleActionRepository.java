package spacemadness.com.lunarconsole.data;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.core.LiveData;
import spacemadness.com.lunarconsole.core.MutableLiveData;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

class ConsoleActionRepository {
	private final List<Action> actions;
	private final MutableLiveData<List<Action>> data;

	ConsoleActionRepository() {
		this(DispatchQueue.mainQueue());
	}

	ConsoleActionRepository(DispatchQueue dispatchQueue) {
		actions = new ArrayList<>();
		data = new MutableLiveData<>(actions, dispatchQueue);
	}

	public Action addAction(int actionId, String actionName) {
		int actionIndex = indexOf(actionName);
		if (actionIndex == -1) {
			Action action = new Action(actionId, actionName);
			actions.add(action);
			notifyObservers();
			return action;
		}

		return actions.get(actionIndex);
	}

	public boolean removeAction(int actionId) {
		for (int actionIndex = actions.size() - 1; actionIndex >= 0; --actionIndex) {
			Action action = actions.get(actionIndex);
			if (action.actionId() == actionId) {
				actions.remove(actionIndex);
				notifyObservers();

				return true;
			}
		}

		return false;
	}

	private void notifyObservers() {
		data.setValue(actions);
	}

	private int indexOf(String name) {
		for (int index = 0; index < actions.size(); ++index) {
			Action action = actions.get(index);
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

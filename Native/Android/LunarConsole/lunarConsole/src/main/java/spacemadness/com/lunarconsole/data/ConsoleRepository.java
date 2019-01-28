package spacemadness.com.lunarconsole.data;

import java.util.List;

import spacemadness.com.lunarconsole.console.Action;
import spacemadness.com.lunarconsole.console.Variable;
import spacemadness.com.lunarconsole.core.LiveData;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class ConsoleRepository {
	private final ConsoleActionRepository actionRepository;
	private final ConsoleVariableRepository variableRepository;

	public ConsoleRepository(ConsoleActionRepository actionRepository, ConsoleVariableRepository variableRepository) {
		this.actionRepository = checkNotNull(actionRepository, "actionRepository");
		this.variableRepository = checkNotNull(variableRepository, "variableRepository");
	}

	public LiveData<List<Action>> getActions() {
		return actionRepository.getActions();
	}

	public LiveData<List<Variable>> getVariables() {
		return variableRepository.getVariables();
	}
}

package spacemadness.com.lunarconsole.console;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.rx.BehaviorSubject;
import spacemadness.com.lunarconsole.rx.Observable;

public class Registrar {
    private final BehaviorSubject<List<Action>> actionSubject;
    private final BehaviorSubject<List<Variable>> variableSubject;

    public Registrar() {
        actionSubject = new BehaviorSubject<>((List<Action>) new ArrayList<Action>());
        variableSubject = new BehaviorSubject<>((List<Variable>) new ArrayList<Variable>());
    }

    public boolean registerAction(Action action) {
        if (!containsAction(action.getActionId())) {
            List<Action> actions = actionSubject.getValue();
            actions.add(action);
            actionSubject.setValue(actions);
            return true;
        }
        return false;
    }

    public boolean unregisterAction(int id) {
        List<Action> actions = actionSubject.getValue();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getActionId() == id) {
                actions.remove(i);
                actionSubject.setValue(actions);
                return true;
            }
        }

        return false;
    }

    public boolean registerVariable(Variable variable) {
        if (!containsVariable(variable.getActionId())) {
            List<Variable> variables = variableSubject.getValue();
            variables.add(variable);
            variableSubject.setValue(variables);
            return true;
        }
        return false;
    }

    public boolean unregisterVariable(int id) {
        List<Variable> variables = variableSubject.getValue();
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getActionId() == id) {
                variables.remove(i);
                variableSubject.setValue(variables);
                return true;
            }
        }

        return false;
    }

    public boolean containsAction(int id) {
        return findAction(id) != null;
    }

    public Action findAction(int id) {
        List<Action> actions = getActions();
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            if (action.getActionId() == id) {
                return action;
            }
        }
        return null;
    }

    public boolean containsVariable(int id) {
        return findVariable(id) != null;
    }

    public Variable findVariable(int id) {
        List<Variable> variables = getVariables();
        for (int i = 0; i < variables.size(); i++) {
            Variable action = variables.get(i);
            if (action.getActionId() == id) {
                return action;
            }
        }
        return null;
    }

    public List<Action> getActions() {
        return actionSubject.getValue();
    }

    public List<Variable> getVariables() {
        return variableSubject.getValue();
    }

    public Observable<List<Action>> getActionStream() {
        return actionSubject;
    }

    public Observable<List<Variable>> getVariableStream() {
        return variableSubject;
    }
}

package spacemadness.com.lunarconsole.console;

import java.util.List;

import spacemadness.com.lunarconsole.rx.Observable;
import spacemadness.com.lunarconsole.rx.Observables;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.CollectionUtils;
import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

public class ConsoleActionViewModel {
    private final Registrar registrar;

    public ConsoleActionViewModel(Registrar registrar) {
        this.registrar = registrar;
    }

    public Observable<List<ListViewItem>> getItemsStream() {
        Observable<List<ListViewItem>> actionStream = Observables.map(registrar.getActionStream(), new MapFunction<List<Action>, List<ListViewItem>>() {
            @Override
            public List<ListViewItem> map(List<Action> actions) {
                return createActionItems(actions);
            }
        });

        Observable<List<ListViewItem>> variableStream = Observables.map(registrar.getVariableStream(), new MapFunction<List<Variable>, List<ListViewItem>>() {
            @Override
            public List<ListViewItem> map(List<Variable> variables) {
                return createVariableItems(variables);
            }
        });
        return actionStream;
    }

    private static List<ListViewItem> createActionItems(List<Action> actions) {
        return CollectionUtils.map(actions, new MapFunction<Action, ListViewItem>() {
            @Override
            public ActionListItem map(Action action) {
                throw new NotImplementedException();
            }
        });
    }

    private List<ListViewItem> createVariableItems(List<Variable> variables) {
        return CollectionUtils.map(variables, new MapFunction<Variable, ListViewItem>() {
            @Override
            public VariableListItem map(Variable variable) {
                throw new NotImplementedException();
            }
        });
    }
}

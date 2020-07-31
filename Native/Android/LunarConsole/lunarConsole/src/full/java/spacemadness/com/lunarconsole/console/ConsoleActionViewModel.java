package spacemadness.com.lunarconsole.console;

import java.util.List;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.StringProvider;
import spacemadness.com.lunarconsole.rx.Observable;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.CollectionUtils;
import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction;

public class ConsoleActionViewModel {
    private final Registrar registrar;
    private final StringProvider strings;

    public ConsoleActionViewModel(Registrar registrar, StringProvider strings) {
        this.registrar = registrar;
        this.strings = strings;
    }

    public Observable<List<ListViewItem>> getItemsStream() {
        Observable<List<ListViewItem>> actionStream = registrar.getActionStream().map(new MapFunction<List<Action>, List<ListViewItem>>() {
            @Override
            public List<ListViewItem> map(List<Action> actions) {
                return createActionItems(actions);
            }
        });

        Observable<List<ListViewItem>> variableStream = registrar.getVariableStream().map(new MapFunction<List<Variable>, List<ListViewItem>>() {
            @Override
            public List<ListViewItem> map(List<Variable> variables) {
                return createVariableItems(variables);
            }
        });

        return Observable
                .combineLatest(actionStream, variableStream)
                .map(new MapFunction<List<List<ListViewItem>>, List<ListViewItem>>() {
                    @Override
                    public List<ListViewItem> map(List<List<ListViewItem>> lists) {
                        return CollectionUtils.merge(lists);
                    }
                });
    }

    private List<ListViewItem> createActionItems(List<Action> actions) {
        List<ListViewItem> items = CollectionUtils.map(actions, new MapFunction<Action, ListViewItem>() {
            @Override
            public ActionListItem map(Action action) {
                return new ActionListItem(action);
            }
        });
        if (items.size() > 0) {
            items.add(0, new HeaderListItem(strings.getString(R.string.header_actions)));
        }
        return items;
    }

    private List<ListViewItem> createVariableItems(List<Variable> variables) {
        List<ListViewItem> items = CollectionUtils.map(variables, new MapFunction<Variable, ListViewItem>() {
            @Override
            public VariableListItem map(Variable variable) {
                return new VariableListItem(variable);
            }
        });
        if (items.size() > 0) {
            items.add(0, new HeaderListItem(strings.getString(R.string.header_variables)));
        }
        return items;
    }
}

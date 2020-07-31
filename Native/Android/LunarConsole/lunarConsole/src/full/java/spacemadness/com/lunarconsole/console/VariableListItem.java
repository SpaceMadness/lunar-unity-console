package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.ui.ListViewItem;

public class VariableListItem extends ListViewItem {
    private final Variable variable;

    public VariableListItem(Variable variable) {
        this.variable = variable;
    }

    @Override
    protected long getItemId() {
        return variable.getActionId();
    }

    public int getId() {
        return variable.getActionId();
    }

    public String getName() {
        return variable.getName();
    }
}

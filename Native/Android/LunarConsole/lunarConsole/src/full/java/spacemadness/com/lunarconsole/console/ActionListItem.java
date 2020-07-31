package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.ui.ListViewItem;

public class ActionListItem extends ListViewItem {
    private final Action action;

    public ActionListItem(Action action) {
        this.action = action;
    }

    @Override
    protected long getItemId() {
        return action.getActionId();
    }

    public int getId() {
        return action.getActionId();
    }

    public String getName() {
        return action.getName();
    }
}

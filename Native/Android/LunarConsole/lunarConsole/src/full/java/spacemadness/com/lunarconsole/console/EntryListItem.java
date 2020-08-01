package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.ui.ListViewItem;

public abstract class EntryListItem extends ListViewItem {
    public final EntryType type;
    public final int id;

    protected EntryListItem(EntryType type, int id) {
        this.type = type;
        this.id = id;
    }

    @Override
    protected int getItemViewType() {
        return type.ordinal();
    }

    @Override
    protected long getItemId() {
        return id;
    }
}

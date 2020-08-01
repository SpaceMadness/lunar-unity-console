package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.ui.ListViewItem;

public abstract class EntryListItem extends ListViewItem {
    public final String name;
    private final EntryType type;
    private final int id;

    protected EntryListItem(EntryType type, int id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
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

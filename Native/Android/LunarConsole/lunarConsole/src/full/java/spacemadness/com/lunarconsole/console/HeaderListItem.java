package spacemadness.com.lunarconsole.console;

import spacemadness.com.lunarconsole.ui.ListViewItem;

import static spacemadness.com.lunarconsole.core.Check.notEmptyArg;

public class HeaderListItem extends ListViewItem {
    public final String title;

    public HeaderListItem(String title) {
        this.title = notEmptyArg(title);
    }
}

package spacemadness.com.lunarconsole.console;

import android.view.View;

import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.ListViewItem;

import static spacemadness.com.lunarconsole.core.Check.notEmptyArg;

public class HeaderListItem extends EntryListItem {
    public final String title;

    public HeaderListItem(String title) {
        super(EntryType.Header, -1);
        this.title = notEmptyArg(title);
    }

    public static class ViewHolder extends ListViewAdapter.ViewHolder<HeaderListItem> {
        public ViewHolder(View convertView) {
            super(convertView);
        }

        @Override
        protected void bindView(HeaderListItem item, int position) {
        }
    }
}

package spacemadness.com.lunarconsole.console;

import android.view.View;

import spacemadness.com.lunarconsole.ui.ListViewAdapter;

public class HeaderListItem extends EntryListItem {
    public HeaderListItem(String title) {
        super(EntryType.Header, -1, title);
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

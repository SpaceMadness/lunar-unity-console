package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;

public class ActionListItem extends EntryListItem {
    public ActionListItem(int id, String name) {
        super(EntryType.Action, id, name);
    }

    private int getBackgroundColor() {
        return 0;
    }

    public static class ViewHolder extends ListViewAdapter.ViewHolder<ActionListItem> {
        private final View layout;
        private final TextView nameView;

        public ViewHolder(View convertView) {
            super(convertView);

            layout = convertView.findViewById(R.id.lunar_console_action_entry_layout);
            nameView = (TextView) convertView.findViewById(R.id.lunar_console_action_entry_name);
        }

        @Override
        protected void bindView(ActionListItem item, int position) {
            layout.setBackgroundColor(item.getBackgroundColor());
            nameView.setText(item.name);
        }
    }
}

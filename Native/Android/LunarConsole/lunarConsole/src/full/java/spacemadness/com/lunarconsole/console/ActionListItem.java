package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.ListViewItem;

public class ActionListItem extends EntryListItem {
    private final Action action;

    public ActionListItem(Action action) {
        super(EntryType.Action, action.getActionId());
        this.action = action;
    }

    public int getId() {
        return action.getActionId();
    }

    public String getName() {
        return action.getName();
    }

    private int getBackgroundColor(Context context, int position) {
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
            Context context = getContext();
            layout.setBackgroundColor(item.getBackgroundColor(context, position));
            nameView.setText(item.getName());
        }
    }
}

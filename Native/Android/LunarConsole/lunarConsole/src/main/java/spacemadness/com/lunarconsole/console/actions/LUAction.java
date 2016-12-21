package spacemadness.com.lunarconsole.console.actions;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleActionAdapter;

public class LUAction extends LUEntry
{
    public LUAction(int actionId, String name)
    {
        super(actionId, name);
    }

    @Override
    public long getItemId()
    {
        return 1; // FIXME: don't use magic number
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    public static class ViewHolder extends ConsoleActionAdapter.ViewHolder<LUAction>
    {
        private final View layout;
        private final TextView nameView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
            nameView = (TextView) itemView.findViewById(R.id.lunar_console_action_entry_name);
        }

        @Override
        public void onBindViewHolder(LUAction action, int position)
        {
            Context context = getContext();
            layout.setBackgroundColor(action.getBackgroundColor(context, position));
            nameView.setText(action.name());
        }
    }
}

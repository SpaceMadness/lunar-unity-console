package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;

public class ActionViewHolder extends ConsoleActionAdapter.ViewHolder<Action>
{
    private final View layout;
    private final TextView nameView;

    public ActionViewHolder(View itemView)
    {
        super(itemView);

        layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
        nameView = (TextView) itemView.findViewById(R.id.lunar_console_action_entry_name);
    }

    @Override
    public void onBindViewHolder(Action action, int position)
    {
        Context context = getContext();
        layout.setBackgroundColor(action.getBackgroundColor(context, position));
        nameView.setText(action.name());
    }
}

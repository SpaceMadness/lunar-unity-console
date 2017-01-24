package spacemadness.com.lunarconsole.console.actions;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleActionAdapter;

/**
 * Created by alementuev on 1/23/17.
 */

public class LUHeaderEntry extends LUEntry
{
    public LUHeaderEntry(String name)
    {
        super(-1, name);
    }

    @Override
    public LUEntryType getEntryType()
    {
        return LUEntryType.Header;
    }

    //region ViewHolder

    public static class ViewHolder extends ConsoleActionAdapter.ViewHolder<LUHeaderEntry>
    {
        private final TextView nameView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.lunar_console_action_entry_name);
        }

        @Override
        public void onBindViewHolder(LUHeaderEntry header, int position)
        {
            nameView.setText(header.name());
        }
    }

    //endregion
}

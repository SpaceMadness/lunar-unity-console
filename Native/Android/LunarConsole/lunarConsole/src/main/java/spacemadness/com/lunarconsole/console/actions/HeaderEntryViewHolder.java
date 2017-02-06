package spacemadness.com.lunarconsole.console.actions;

import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleActionAdapter;

public class HeaderEntryViewHolder extends ConsoleActionAdapter.ViewHolder<HeaderEntry>
{
    private final TextView nameView;

    public HeaderEntryViewHolder(View itemView)
    {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.lunar_console_header_entry_name);
    }

    @Override
    public void onBindViewHolder(HeaderEntry header, int position)
    {
        nameView.setText(header.name());
    }
}

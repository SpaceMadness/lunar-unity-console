package spacemadness.com.lunarconsole.console;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spacemadness.com.lunarconsole.R;

public class ConsoleOverlayAdapter extends BaseConsoleAdapter
{
    public ConsoleOverlayAdapter(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    protected ViewHolder createViewHolder(View convertView)
    {
        return new ConsoleEntry.OverlayViewHolder(convertView);
    }

    @Override
    protected View createConvertView(ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.lunar_layout_overlay_log_entry, parent, false);
    }
}

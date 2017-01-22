package spacemadness.com.lunarconsole.console;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.actions.LUAction;

public class ConsoleActionAdapter extends BaseConsoleActionAdapter<LUAction>
{
    public ConsoleActionAdapter(DataSource<LUAction> dataSource)
    {
        super(dataSource);
    }

    @Override
    protected ViewHolder createViewHolder(View convertView)
    {
        return new LUAction.ViewHolder(convertView);
    }

    @Override
    protected View createConvertView(ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.lunar_console_layout_console_action_entry, parent, false);
    }
}

package spacemadness.com.lunarconsole.console;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public class ConsoleEntry
{
    public int index;
    public final byte type;
    public final String message;
    public final String stackTrace;

    public ConsoleEntry(byte type, String message)
    {
        this(type, message, null);
    }

    public ConsoleEntry(byte type, String message, String stackTrace)
    {
        this.type = type;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    public static class ViewHolder extends ConsoleAdapter.ViewHolder<ConsoleEntry>
    {
        private final View layout;
        private final ImageView iconView;
        private final TextView messageView;

        private static final int[] LOG_ENTRY_ICON_RES_LOOKUP = new int[COUNT];

        static
        {
            LOG_ENTRY_ICON_RES_LOOKUP[ERROR] = R.drawable.lunar_console_icon_log_error;
            LOG_ENTRY_ICON_RES_LOOKUP[ASSERT] = R.drawable.lunar_console_icon_log_error;
            LOG_ENTRY_ICON_RES_LOOKUP[WARNING] = R.drawable.lunar_console_icon_log_warning;
            LOG_ENTRY_ICON_RES_LOOKUP[LOG] = R.drawable.lunar_console_icon_log;
            LOG_ENTRY_ICON_RES_LOOKUP[EXCEPTION] = R.drawable.lunar_console_icon_log_error;
        }

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_log_entry_layout);
            iconView = (ImageView) itemView.findViewById(R.id.lunar_console_log_entry_icon);
            messageView = (TextView) itemView.findViewById(R.id.lunar_console_log_entry_message);
        }

        @Override
        public void onBindViewHolder(ConsoleEntry entry)
        {
            int backgroundColor = entry.index % 2 == 0 ?
                    getColor(R.color.lunar_console_color_cell_background_dark) :
                    getColor(R.color.lunar_console_color_cell_background_light);
            layout.setBackgroundColor(backgroundColor);

            iconView.setImageDrawable(getIconResDrawable(entry.type));
            messageView.setText(entry.message);
        }

        @SuppressWarnings("deprecation")
        private Drawable getIconResDrawable(int type)
        {
            int id = getIconResId(type);
            return getResources().getDrawable(id);
        }

        private int getIconResId(int type)
        {
            return type >= 0 && type < LOG_ENTRY_ICON_RES_LOOKUP.length ?
                    LOG_ENTRY_ICON_RES_LOOKUP[type] : R.drawable.lunar_console_icon_log;
        }
    }
}
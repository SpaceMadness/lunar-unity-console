//
//  ConsoleEntry.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

public class ConsoleEntry
{
    private static final int[] LOG_ENTRY_ICON_RES_LOOKUP = new int[COUNT];

    static
    {
        LOG_ENTRY_ICON_RES_LOOKUP[ERROR] = R.drawable.lunar_console_icon_log_error;
        LOG_ENTRY_ICON_RES_LOOKUP[ASSERT] = R.drawable.lunar_console_icon_log_error;
        LOG_ENTRY_ICON_RES_LOOKUP[WARNING] = R.drawable.lunar_console_icon_log_warning;
        LOG_ENTRY_ICON_RES_LOOKUP[LOG] = R.drawable.lunar_console_icon_log;
        LOG_ENTRY_ICON_RES_LOOKUP[EXCEPTION] = R.drawable.lunar_console_icon_log_error;
    }

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

    @SuppressWarnings("deprecation")
    public Drawable getIconDrawable(Context context)
    {
        int id = getIconResId(type);
        return context.getResources().getDrawable(id);
    }

    @SuppressWarnings("deprecation")
    public int getBackgroundColor(Context context)
    {
        int colorId = index % 2 == 0 ?
                R.color.lunar_console_color_cell_background_dark :
                R.color.lunar_console_color_cell_background_light;
        return context.getResources().getColor(colorId);
    }

    public boolean hasStackTrace()
    {
        return stackTrace != null && stackTrace.length() > 0;
    }

    private int getIconResId(int type)
    {
        return type >= 0 && type < LOG_ENTRY_ICON_RES_LOOKUP.length ?
                LOG_ENTRY_ICON_RES_LOOKUP[type] : R.drawable.lunar_console_icon_log;
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    public static class ViewHolder extends ConsoleAdapter.ViewHolder<ConsoleEntry>
    {
        private final View layout;
        private final ImageView iconView;
        private final TextView messageView;

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
            Context context = getContext();
            layout.setBackgroundColor(entry.getBackgroundColor(context));
            iconView.setImageDrawable(entry.getIconDrawable(context));
            messageView.setText(entry.message);
        }
    }
}
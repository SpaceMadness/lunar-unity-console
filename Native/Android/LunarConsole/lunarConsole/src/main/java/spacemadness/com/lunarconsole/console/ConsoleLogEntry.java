//
//  ConsoleLogEntry.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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
import spacemadness.com.lunarconsole.utils.ObjectUtils;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

/**
 * Class for representing console log messages
 */
public class ConsoleLogEntry extends BaseEntry
{
    private static final Appearance APPEARANCE_LOG          = new Appearance(R.drawable.lunar_console_icon_log, R.color.lunar_console_color_overlay_entry_log);
    private static final Appearance APPEARANCE_LOG_ERROR    = new Appearance(R.drawable.lunar_console_icon_log_error, R.color.lunar_console_color_overlay_entry_log_error);
    private static final Appearance APPEARANCE_LOG_WARNING  = new Appearance(R.drawable.lunar_console_icon_log_warning, R.color.lunar_console_color_overlay_entry_log_warning);

    private static final Appearance[] LOG_ENTRY_ICON_RES_LOOKUP = new Appearance[COUNT];

    static
    {
        LOG_ENTRY_ICON_RES_LOOKUP[ERROR]        = APPEARANCE_LOG_ERROR;
        LOG_ENTRY_ICON_RES_LOOKUP[ASSERT]       = APPEARANCE_LOG_ERROR;
        LOG_ENTRY_ICON_RES_LOOKUP[WARNING]      = APPEARANCE_LOG_WARNING;
        LOG_ENTRY_ICON_RES_LOOKUP[LOG]          = APPEARANCE_LOG;
        LOG_ENTRY_ICON_RES_LOOKUP[EXCEPTION]    = APPEARANCE_LOG_ERROR;
    }

    public int index;
    public final byte type;
    public final String message;
    public final String stackTrace;

    /** For testing purposes */
    ConsoleLogEntry(String message)
    {
        this(ConsoleLogType.LOG, message, "");
    }

    public ConsoleLogEntry(byte type, String message)
    {
        this(type, message, null);
    }

    public ConsoleLogEntry(byte type, String message, String stackTrace)
    {
        this.type = type;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    @Override
    public long getItemId()
    {
        return type;
    }

    @SuppressWarnings("deprecation")
    public Drawable getIconDrawable(Context context)
    {
        int id = getAppearance(type).iconId;
        return context.getResources().getDrawable(id);
    }

    @SuppressWarnings("deprecation")
    public int getBackgroundColor(Context context, int position)
    {
        int colorId = position % 2 == 0 ?
                R.color.lunar_console_color_cell_background_dark :
                R.color.lunar_console_color_cell_background_light;
        return context.getResources().getColor(colorId);
    }

    public boolean hasStackTrace()
    {
        return stackTrace != null && stackTrace.length() > 0;
    }

    static Appearance getAppearance(int type)
    {
        return type >= 0 && type < LOG_ENTRY_ICON_RES_LOOKUP.length ?
                LOG_ENTRY_ICON_RES_LOOKUP[type] : APPEARANCE_LOG;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Entry appearance

    static class Appearance
    {
        public final int iconId;
        public final int overlayColorId;

        Appearance(int iconId, int overlayColorId)
        {
            this.iconId = iconId;
            this.overlayColorId = overlayColorId;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    public static class ViewHolder extends ConsoleLogAdapter.ViewHolder<ConsoleLogEntry>
    {
        private final View layout;
        private final ImageView iconView;
        private final TextView messageView;
        private final TextView collapsedCountView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_log_entry_layout);
            iconView = (ImageView) itemView.findViewById(R.id.lunar_console_log_entry_icon);
            messageView = (TextView) itemView.findViewById(R.id.lunar_console_log_entry_message);
            collapsedCountView = (TextView) itemView.findViewById(R.id.lunar_console_log_collapsed_count);
        }

        @Override
        public void onBindViewHolder(ConsoleLogEntry entry, int position)
        {
            Context context = getContext();
            layout.setBackgroundColor(entry.getBackgroundColor(context, position));
            iconView.setImageDrawable(entry.getIconDrawable(context));
            messageView.setText(entry.message);

            ConsoleCollapsedLogEntry collapsedEntry = ObjectUtils.as(entry, ConsoleCollapsedLogEntry.class);
            if (collapsedEntry != null && collapsedEntry.count > 1)
            {
                collapsedCountView.setVisibility(View.VISIBLE);
                collapsedCountView.setText(Integer.toString(collapsedEntry.count));
            }
            else
            {
                collapsedCountView.setVisibility(View.GONE);
            }
        }
    }

}
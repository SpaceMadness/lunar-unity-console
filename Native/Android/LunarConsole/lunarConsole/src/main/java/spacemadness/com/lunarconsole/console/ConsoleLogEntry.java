//
//  ConsoleLogEntry.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.utils.IntReference;
import spacemadness.com.lunarconsole.utils.NotImplementedException;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;

/**
 * Class for representing console log messages
 */
public class ConsoleLogEntry extends BaseEntry {
    private static final Appearance APPEARANCE_LOG = new Appearance(R.drawable.lunar_console_icon_log, R.color.lunar_console_color_overlay_entry_log);
    private static final Appearance APPEARANCE_LOG_ERROR = new Appearance(R.drawable.lunar_console_icon_log_error, R.color.lunar_console_color_overlay_entry_log_error);
    private static final Appearance APPEARANCE_LOG_WARNING = new Appearance(R.drawable.lunar_console_icon_log_warning, R.color.lunar_console_color_overlay_entry_log_warning);

    private static final Appearance[] LOG_ENTRY_ICON_RES_LOOKUP = new Appearance[COUNT];

    static {
        LOG_ENTRY_ICON_RES_LOOKUP[ERROR] = APPEARANCE_LOG_ERROR;
        LOG_ENTRY_ICON_RES_LOOKUP[ASSERT] = APPEARANCE_LOG_ERROR;
        LOG_ENTRY_ICON_RES_LOOKUP[WARNING] = APPEARANCE_LOG_WARNING;
        LOG_ENTRY_ICON_RES_LOOKUP[LOG] = APPEARANCE_LOG;
        LOG_ENTRY_ICON_RES_LOOKUP[EXCEPTION] = APPEARANCE_LOG_ERROR;
    }

    public int index;
    public final byte type;
    public final String message;
    final /* Nullable */ Spanned spannedMessage;
    public final String stackTrace;

    /**
     * For testing purposes
     */
    ConsoleLogEntry(String message) {
        this(ConsoleLogType.LOG, message, "");
    }

    public ConsoleLogEntry(byte type, String message) {
        this(type, message, null);
    }

    public ConsoleLogEntry(byte type, String message, String stackTrace) {
        this(type, message, null, stackTrace);
    }

    public ConsoleLogEntry(byte type, String message, Spanned spannedMessage, String stackTrace) {
        this.type = type;
        this.message = message;
        this.spannedMessage = spannedMessage;
        this.stackTrace = stackTrace;
    }

    //region Rich Text

    private static boolean _isvalidTagName(String name)
    {
        return name.equals("b") || name.equals("i") || name.equals("color");
    }

    private static LURichTextTagInfo _tryCaptureTag(String str, int position, IntReference iterPtr)
    {
        int end = iterPtr.value;
        boolean isOpen = true;
        if (end < str.length() && str.charAt(end) == '/')
        {
            isOpen = false;
            ++end;
        }

        int start = end;
        boolean found = false;
        while (end < str.length())
        {
            char chr = str.charAt(end++);
            if (chr == '>')
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            return null;
        }

        String capture = str.substring(start, end - 1);
        int index = capture.lastIndexOf('=');
        String name = index != -1 ? capture.substring(0, index) : capture;
        if (!_isvalidTagName(name))
        {
            return null;
        }

        String attribute = index != -1 ? capture.substring(index + 1) : null;
        iterPtr.value = end;
        return new LURichTextTagInfo(name, attribute, isOpen, position);
    }

    private static StyleSpan bold = new StyleSpan(Typeface.BOLD);
    private static StyleSpan italic = new StyleSpan(Typeface.ITALIC);
    private static StyleSpan boldItalic = new StyleSpan(Typeface.BOLD_ITALIC);

    public static ConsoleLogEntry fromRichText(byte type, String text, String stackTrace)
    {
        List<LURichTextTag> tags = null;
        Stack<LURichTextTagInfo> stack = null;
        IntReference i = new IntReference(0);

        StringBuilder buffer = new StringBuilder(text.length());

        int boldCount = 0;
        int italicCount = 0;

        while (i.value < text.length())
        {
            char chr = text.charAt(i.value++);
            if (chr == '<')
            {
                LURichTextTagInfo tag = _tryCaptureTag(text, buffer.length(), i);
                if (tag != null)
                {
                    if (tag.open)
                    {
                        if ("b".equals(tag.name))
                        {
                            boldCount++;
                        }
                        else if ("i".equals(tag.name))
                        {
                            italicCount++;
                        }

                        if (stack == null) stack = new Stack<>();
                        stack.add(tag);
                    }
                    else if (stack != null && stack.size() > 0)
                    {
                        LURichTextTagInfo opposingTag = stack.pop();

                        // if tags don't match - just use raw string
                        if (!tag.name.equals(opposingTag.name))
                        {
                            continue;
                        }

                        if ("b".equals(tag.name))
                        {
                            boldCount--;
                            if (boldCount > 0)
                            {
                                continue;
                            }
                        }
                        else if ("i".equals(tag.name))
                        {
                            italicCount--;
                            if (italicCount > 0)
                            {
                                continue;
                            }
                        }

                        // create rich text tag
                        int len = buffer.length() - opposingTag.position;
                        if (len > 0)
                        {
                            if (tags == null) tags = new ArrayList<>();
                            switch (tag.name) {
                                case "b": {
                                    StyleSpan style = italicCount > 0 ? boldItalic : bold;
                                    tags.add(new LURichTextTag(style, opposingTag.position, len));
                                    break;
                                }
                                case "i": {
                                    StyleSpan style = boldCount > 0 ? boldItalic : italic;
                                    tags.add(new LURichTextTag(style, opposingTag.position, len));
                                    break;
                                }
                                case "color":
                                    String colorValue = opposingTag.attribute;
                                    if (colorValue != null) {
                                        CharacterStyle style = _parseColor(colorValue);
                                        tags.add(new LURichTextTag(style, opposingTag.position, len));
                                    }
                                    break;
                            }
                        }
                    }
                }
                else
                {
                    buffer.append(chr);
                }
            }
            else
            {
                buffer.append(chr);
            }
        }

        if (tags != null && buffer.length() > 0)
        {
            // [[self alloc] initWithText:[[NSString alloc] initWithCharacters:buffer length:bufferSize] tags:tags];
            throw new NotImplementedException();
        }

        if (buffer.length() < text.length())
        {
            text = buffer.toString();
        }

        return new ConsoleLogEntry(type, text, stackTrace);
    }

    private static CharacterStyle _parseColor(String colorValue) {
        return new ForegroundColorSpan(0xff0000);
    }

    private static class LURichTextTag
    {
        public final Object style;
        public final int startIndex;
        public final int endIndex;

        public LURichTextTag(CharacterStyle style, int startIndex, int endIndex) {
            this((Object) style, startIndex, endIndex);
        }

        public LURichTextTag(StyleSpan style, int startIndex, int endIndex) {
            this((Object) style, startIndex, endIndex);
        }

        private LURichTextTag(Object style, int startIndex, int endIndex) {
            this.style = style;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }

    private static final class LURichTextTagInfo
    {
        public final String name;
        public final String attribute;
        public final boolean open;
        public final int position;

        private LURichTextTagInfo(String name, String attribute, boolean open, int position) {
            this.name = name;
            this.attribute = attribute;
            this.open = open;
            this.position = position;
        }
    }

    //endregion

    public CharSequence getMessage()
    {
        return spannedMessage != null ? spannedMessage : message;
    }

    @Override
    public long getItemId() {
        return type;
    }

    @SuppressWarnings("deprecation")
    public Drawable getIconDrawable(Context context) {
        int id = getAppearance(type).iconId;
        return context.getResources().getDrawable(id);
    }

    @SuppressWarnings("deprecation")
    public int getBackgroundColor(Context context, int position) {
        int colorId = position % 2 == 0 ?
                R.color.lunar_console_color_cell_background_dark :
                R.color.lunar_console_color_cell_background_light;
        return context.getResources().getColor(colorId);
    }

    public boolean hasStackTrace() {
        return stackTrace != null && stackTrace.length() > 0;
    }

    static Appearance getAppearance(int type) {
        return type >= 0 && type < LOG_ENTRY_ICON_RES_LOOKUP.length ?
                LOG_ENTRY_ICON_RES_LOOKUP[type] : APPEARANCE_LOG;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Entry appearance

    static class Appearance {
        public final int iconId;
        public final int overlayColorId;

        Appearance(int iconId, int overlayColorId) {
            this.iconId = iconId;
            this.overlayColorId = overlayColorId;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    public static class ViewHolder extends ConsoleLogAdapter.ViewHolder<ConsoleLogEntry> {
        private final View layout;
        private final ImageView iconView;
        private final TextView messageView;
        private final TextView collapsedCountView;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_log_entry_layout);
            iconView = (ImageView) itemView.findViewById(R.id.lunar_console_log_entry_icon);
            messageView = (TextView) itemView.findViewById(R.id.lunar_console_log_entry_message);
            collapsedCountView = (TextView) itemView.findViewById(R.id.lunar_console_log_collapsed_count);
        }

        @Override
        public void onBindViewHolder(ConsoleLogEntry entry, int position) {
            Context context = getContext();
            layout.setBackgroundColor(entry.getBackgroundColor(context, position));
            iconView.setImageDrawable(entry.getIconDrawable(context));
            messageView.setText(entry.getMessage());

            ConsoleCollapsedLogEntry collapsedEntry = ObjectUtils.as(entry, ConsoleCollapsedLogEntry.class);
            if (collapsedEntry != null && collapsedEntry.count > 1) {
                collapsedCountView.setVisibility(View.VISIBLE);
                collapsedCountView.setText(Integer.toString(collapsedEntry.count));
            } else {
                collapsedCountView.setVisibility(View.GONE);
            }
        }
    }

}
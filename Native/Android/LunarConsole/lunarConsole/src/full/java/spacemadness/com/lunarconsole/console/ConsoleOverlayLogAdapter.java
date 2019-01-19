//
//  ConsoleOverlayLogAdapter.java
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;

public class ConsoleOverlayLogAdapter extends BaseConsoleLogAdapter
{
    public ConsoleOverlayLogAdapter(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    protected ViewHolder createViewHolder(View convertView, int position)
    {
        return new OverlayViewHolder(convertView);
    }

    @Override
    protected View createConvertView(ViewGroup parent, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.lunar_console_layout_overlay_log_entry, parent, false);
    }

    private static class OverlayViewHolder extends ViewHolder<ConsoleLogEntry>
    {
        private final TextView messageView;

        public OverlayViewHolder(View itemView)
        {
            super(itemView);
            messageView = (TextView) itemView.findViewById(R.id.lunar_console_overlay_log_entry_message);
        }

        @Override
        public void onBindViewHolder(ConsoleLogEntry entry, int position)
        {
            final int colorId = ConsoleLogEntry.getAppearance(entry.type).overlayColorId;

            messageView.setText(entry.message);
            messageView.setTextColor(getColor(colorId));
        }
    }
}

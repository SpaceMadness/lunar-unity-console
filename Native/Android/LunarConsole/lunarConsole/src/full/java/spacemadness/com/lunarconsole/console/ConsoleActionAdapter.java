//
//  ConsoleActionAdapter.java
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

import spacemadness.com.lunarconsole.R;

public class ConsoleActionAdapter extends BaseConsoleActionAdapter<IdentityEntry>
{
    public ConsoleActionAdapter(DataSource<IdentityEntry> dataSource)
    {
        super(dataSource);
    }

    @Override
    protected ViewHolder createViewHolder(View convertView, int position)
    {
        EntryType type = getEntryType(position);
        switch (type)
        {
            case Action:
                return new ActionViewHolder(convertView);
            case Variable:
                return new VariableViewHolder(convertView);
            case Header:
                return new HeaderEntryViewHolder(convertView);
        }

        throw new IllegalStateException("Unexpected entry type: " + type);
    }

    @Override
    protected View createConvertView(ViewGroup parent, int position)
    {
        EntryType type = getEntryType(position);
        int layoutId;
        switch (type)
        {
            case Action:
                layoutId = R.layout.lunar_console_layout_console_action_entry;
                break;
            case Variable:
                layoutId = R.layout.lunar_console_layout_console_variable_entry;
                break;
            case Header:
                layoutId = R.layout.lunar_console_layout_console_header_entry;
                break;
            default:
                throw new IllegalStateException("Unexpected entry type: " + type);
        }

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(layoutId, parent, false);
    }

    @Override
    public int getItemViewType(int position)
    {
        return getEntryType(position).ordinal();
    }

    @Override
    public int getViewTypeCount()
    {
        return EntryType.values().length;
    }

    private EntryType getEntryType(int position)
    {
        IdentityEntry entry = (IdentityEntry) getItem(position);
        return entry.getEntryType();
    }
}

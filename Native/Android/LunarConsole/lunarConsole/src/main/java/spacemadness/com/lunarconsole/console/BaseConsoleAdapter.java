//
//  ConsoleAdapter.java
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
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseConsoleAdapter extends BaseAdapter
{
    private DataSource dataSource;

    public BaseConsoleAdapter(DataSource dataSource)
    {
        if (dataSource == null)
        {
            throw new NullPointerException("Data source is null");
        }

        this.dataSource = dataSource;
    }

    @Override
    public int getCount()
    {
        return dataSource.getEntryCount();
    }

    @Override
    public Object getItem(int position)
    {
        return dataSource.getEntry(position);
    }

    @Override
    public long getItemId(int position)
    {
        return dataSource.getEntry(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = createConvertView(parent);
            viewHolder = createViewHolder(convertView);

            convertView.setTag(viewHolder);
        }

        ConsoleEntry entry = dataSource.getEntry(position);
        viewHolder.bindViewHolder(entry);

        return convertView;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Data Source

    public interface DataSource
    {
        ConsoleEntry getEntry(int position);
        int getEntryCount();
    }

    //////////////////////////////////////////////////////////////////////////////
    // View Holder

    protected abstract ViewHolder createViewHolder(View convertView);

    protected abstract View createConvertView(ViewGroup parent);

    public static abstract class ViewHolder<T extends ConsoleEntry>
    {
        protected final View itemView;

        public ViewHolder(View itemView)
        {
            this.itemView = itemView;
        }

        void bindViewHolder(ConsoleEntry entry)
        {
            onBindViewHolder((T) entry);
        }

        public abstract void onBindViewHolder(T entry);

        protected Context getContext()
        {
            return itemView.getContext();
        }

        protected Resources getResources()
        {
            return getContext().getResources();
        }

        protected String getString(int id)
        {
            return getResources().getString(id);
        }

        protected int getColor(int id)
        {
            return getResources().getColor(id);
        }
    }
}

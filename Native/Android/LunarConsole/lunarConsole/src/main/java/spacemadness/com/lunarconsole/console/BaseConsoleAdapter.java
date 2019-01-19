//
//  BaseConsoleAdapter.java
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
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

abstract class BaseConsoleAdapter<T extends BaseEntry> extends BaseAdapter
{
    private final DataSource<T> dataSource;

    BaseConsoleAdapter(DataSource<T> dataSource)
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
        return dataSource.getEntry(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder<T> viewHolder;
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = createConvertView(parent, position);
            viewHolder = createViewHolder(convertView, position);

            convertView.setTag(viewHolder);
        }

        T entry = dataSource.getEntry(position);
        viewHolder.bindViewHolder(entry, position);

        return convertView;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Data Source

    public interface DataSource<E extends BaseEntry>
    {
        E getEntry(int position);
        int getEntryCount();
    }

    //////////////////////////////////////////////////////////////////////////////
    // View Holder

    protected abstract ViewHolder createViewHolder(View convertView, int position);

    protected abstract View createConvertView(ViewGroup parent, int position);

    public static abstract class ViewHolder<T extends BaseEntry>
    {
        protected final View itemView;

        public ViewHolder(View itemView)
        {
            this.itemView = itemView;
        }

        void bindViewHolder(T entry, int position)
        {
            onBindViewHolder(entry, position);
        }

        public abstract void onBindViewHolder(T entry, int position);

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
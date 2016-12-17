package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

abstract class BaseConsoleAdapter<T extends ConsoleEntry> extends BaseAdapter
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
            convertView = createConvertView(parent);
            viewHolder = createViewHolder(convertView);

            convertView.setTag(viewHolder);
        }

        T entry = dataSource.getEntry(position);
        viewHolder.bindViewHolder(entry);

        return convertView;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Data Source

    public interface DataSource<E extends ConsoleEntry>
    {
        E getEntry(int position);
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

        void bindViewHolder(T entry)
        {
            onBindViewHolder(entry);
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
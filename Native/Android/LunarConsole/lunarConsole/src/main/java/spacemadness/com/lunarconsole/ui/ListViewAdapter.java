package spacemadness.com.lunarconsole.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class ListViewAdapter extends BaseAdapter {
	private final List<ListViewItem> items;
	private final Map<Integer, Factory> lookup;

	public ListViewAdapter(List<ListViewItem> items) {
		this.items = checkNotNull(items, "items");
		this.lookup = new HashMap<>();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public ListViewItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getItemId();
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getItemViewType();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			final int itemViewType = getItemViewType(position);
			final Factory factory = getFactory(itemViewType);

			convertView = factory.createConvertView(parent);
			viewHolder = factory.createViewHolder(convertView);

			convertView.setTag(viewHolder);
		}

		final Object item = getItem(position);
		//noinspection unchecked
		viewHolder.bindView(item, position);

		return convertView;
	}

	public void register(Enum<?> itemType, Factory factory) {
		register(itemType.ordinal(), factory);
	}

	public void register(int itemType, Factory factory) {
		lookup.put(itemType, checkNotNull(factory, "factory"));
	}

	private Factory getFactory(int itemType) {
		Factory factory = lookup.get(itemType);
		if (factory == null) {
			throw new IllegalArgumentException("Item type not registered: " + itemType);
		}
		return factory;
	}

	public interface Factory {
		View createConvertView(ViewGroup parent);

		ViewHolder createViewHolder(View convertView);
	}

	public static abstract class LayoutIdFactory implements Factory {
		private final int layoutId;

		public LayoutIdFactory(int layoutId) {
			this.layoutId = layoutId;
		}

		@Override
		public View createConvertView(ViewGroup parent) {
			return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
		}
	}

	public abstract static class ViewHolder<T> {
		private final View view;

		public ViewHolder(View view) {
			this.view = checkNotNull(view, "view");
		}

		protected abstract void bindView(T item, int position);
	}
}


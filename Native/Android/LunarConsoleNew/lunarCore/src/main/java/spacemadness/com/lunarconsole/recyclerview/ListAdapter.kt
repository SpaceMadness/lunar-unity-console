package spacemadness.com.lunarconsole.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

class ListAdapter : androidx.recyclerview.widget.ListAdapter<ListItem, ViewHolder<*>>(diff) {
    private val viewHolderFactoryLookup = mutableMapOf<Int, ViewHolderFactory>()

    fun register(viewType: Int, factory: ViewHolderFactory) {
        viewHolderFactoryLookup[viewType] = factory
    }

    fun register(viewType: Enum<*>, factory: ViewHolderFactory) {
        viewHolderFactoryLookup[viewType.ordinal] = factory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        val factory = viewHolderFactoryLookup[viewType]
            ?: throw IllegalStateException("View type not registered: $viewType")
        return factory.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) =
                oldItem.isSame(newItem)

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) =
                oldItem.isContentsSame(newItem)
        }
    }
}
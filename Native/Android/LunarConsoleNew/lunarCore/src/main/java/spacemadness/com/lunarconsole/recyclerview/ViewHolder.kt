package spacemadness.com.lunarconsole.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T : ListItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal fun bind(item: ListItem, position: Int) {
        @Suppress("UNCHECKED_CAST")
        onBind(item as T, position)
    }

    internal fun recycle() {
        onRecycle()
    }

    protected abstract fun onBind(item: T, position: Int)

    protected open fun onRecycle() {}
}
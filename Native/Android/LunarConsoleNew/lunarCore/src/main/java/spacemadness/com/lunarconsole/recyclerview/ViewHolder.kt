package spacemadness.com.lunarconsole.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import spacemadness.com.lunarconsole.core.Disposable

abstract class ViewHolder<T : ListItem>(itemView: View) : RecyclerView.ViewHolder(itemView), Disposable {
    fun bind(item: ListItem, position: Int) {
        @Suppress("UNCHECKED_CAST")
        onBind(item as T, position)
    }

    override fun dispose() {
        onDispose()
    }

    protected abstract fun onBind(item: T, position: Int)

    protected open fun onDispose() {}
}
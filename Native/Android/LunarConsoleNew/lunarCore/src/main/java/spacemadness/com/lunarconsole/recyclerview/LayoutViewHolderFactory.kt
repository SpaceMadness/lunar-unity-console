package spacemadness.com.lunarconsole.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LayoutViewHolderFactory(
    private val layoutId: Int,
    private val creator: (itemView: View) -> ViewHolder<*>
) : ViewHolderFactory {
    override fun createViewHolder(parent: ViewGroup): ViewHolder<*> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return creator(itemView)
    }
}
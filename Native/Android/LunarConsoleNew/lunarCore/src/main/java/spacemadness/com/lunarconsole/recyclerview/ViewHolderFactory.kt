package spacemadness.com.lunarconsole.recyclerview

import android.view.ViewGroup

interface ViewHolderFactory {
    fun createViewHolder(parent: ViewGroup): ViewHolder<*>
}
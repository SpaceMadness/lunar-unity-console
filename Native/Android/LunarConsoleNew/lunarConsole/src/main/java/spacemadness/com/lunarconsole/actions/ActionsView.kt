package spacemadness.com.lunarconsole.actions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lunar_console_layout_console_action_view.view.*
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.recyclerview.LayoutViewHolderFactory
import spacemadness.com.lunarconsole.recyclerview.ListAdapter
import spacemadness.com.lunarconsole.recyclerview.ViewHolder
import spacemadness.com.lunarconsole.recyclerview.ViewHolderFactory
import spacemadness.com.lunarconsole.ui.AbstractLayout

class ActionsView(context: Context) : AbstractLayout(context) {
    init {
        View.inflate(context, R.layout.lunar_console_layout_console_action_view, this)

        val adapter = createListAdapter()

        val recyclerView = recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun createListAdapter() =
        ListAdapter().apply {
            // actions
            register(
                viewType = ItemType.Action,
                factory = LayoutViewHolderFactory(R.layout.lunar_console_layout_console_action_entry) { itemView ->
                    object : ViewHolder<ActionItem>(itemView) {
                        private val nameText =
                            itemView.findViewById<TextView>(R.id.lunar_console_action_entry_name)

                        override fun onBind(item: ActionItem, position: Int) {
                            nameText.text = item.action.name
                        }
                    }
                })
        }
}

private enum class ItemType {
    Header,
    Group,
    Action,
    Variable
}

private abstract class ListItem(type: ItemType) :
    spacemadness.com.lunarconsole.recyclerview.ListItem() {
    override val viewType = type.ordinal
}

private object HeaderItem : ListItem(ItemType.Header)
private data class GroupItem(val title: String, val collapsed: Boolean) : ListItem(ItemType.Group)
private data class ActionItem(val action: Action) : ListItem(ItemType.Action)
private data class VariableItem(val variable: Variable<*>) : ListItem(ItemType.Variable)
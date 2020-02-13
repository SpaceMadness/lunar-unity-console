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

class ActionsView(context: Context, viewModel: ActionsViewModel) : AbstractLayout(context) {
    init {
        View.inflate(context, R.layout.lunar_console_layout_console_action_view, this)

        val adapter = createListAdapter()

        val recyclerView = recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.items.subscribe { adapter.submitList(it) }
    }

    private fun createListAdapter() =
        ListAdapter().apply {
            // header
            register(
                viewType = ItemType.Header,
                factory = LayoutViewHolderFactory(R.layout.lunar_console_layout_console_header_entry) { itemView ->
                    object : ViewHolder<HeaderItem>(itemView) {
                        private val nameText =
                            itemView.findViewById<TextView>(R.id.lunar_console_header_entry_name)

                        override fun onBind(item: HeaderItem, position: Int) {
                            nameText.text = item.title
                        }
                    }
                })
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
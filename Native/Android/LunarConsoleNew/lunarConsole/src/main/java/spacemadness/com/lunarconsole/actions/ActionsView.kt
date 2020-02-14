package spacemadness.com.lunarconsole.actions

import android.content.Context
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lunar_console_layout_console_action_view.view.*
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.extensions.isVisible
import spacemadness.com.lunarconsole.extensions.setPadding
import spacemadness.com.lunarconsole.recyclerview.LayoutViewHolderFactory
import spacemadness.com.lunarconsole.recyclerview.ListAdapter
import spacemadness.com.lunarconsole.recyclerview.ViewHolder
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
            // group
            register(
                viewType = ItemType.Group,
                factory = LayoutViewHolderFactory(R.layout.lunar_console_layout_console_group_entry) { itemView ->
                    object : ViewHolder<GroupItem>(itemView) {
                        private val nameText =
                            itemView.findViewById<TextView>(R.id.lunar_console_action_group_name)

                        override fun onBind(item: GroupItem, position: Int) {
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
                            // name
                            nameText.text = item.action.name

                            // indent
                            if (item.action.hasGroup) {
                                nameText.setPadding(left = 2 * nameText.paddingLeft)
                            }

                            // click listener
                            itemView.setOnClickListener {  }
                        }
                    }
                })
            // variables
            register(
                viewType = ItemType.Variable,
                factory = LayoutViewHolderFactory(R.layout.lunar_console_layout_console_variable_entry) { itemView ->
                    object : ViewHolder<VariableItem>(itemView) {
                        private val nameText =
                            itemView.findViewById<TextView>(R.id.lunar_console_variable_entry_name)

                        private val valueEdit =
                            itemView.findViewById<EditText>(R.id.lunar_console_variable_entry_value)

                        private val valueSwitch =
                            itemView.findViewById<Switch>(R.id.lunar_console_variable_entry_switch)

                        private val saveButton =
                            itemView.findViewById<ImageButton>(R.id.lunar_console_variable_button_save)

                        private val discardButton =
                            itemView.findViewById<ImageButton>(R.id.lunar_console_variable_button_discard)

                        private val resetButton =
                            itemView.findViewById<ImageButton>(R.id.lunar_console_variable_button_reset)

                        override fun onBind(item: VariableItem, position: Int) {
                            val variable = item.variable

                            // name
                            nameText.text = variable.name

                            // value
                            if (variable is NumericVariable<*> ||
                                variable is StringVariable ||
                                variable is EnumVariable
                            ) {
                                valueEdit.setText(variable.value.toString())
                                valueSwitch.isVisible = false
                            } else if (variable is BooleanVariable) {
                                valueEdit.isVisible = false
                                valueSwitch.isChecked = variable.value
                            } else {
                                // FIXME: don't crash in production - just show some error in UI
                                throw IllegalStateException("Unexpected variable: $variable")
                            }

                            // default value
                            resetButton.isVisible = !variable.isDefault()

                            // control buttons
                            saveButton.isVisible = false
                            discardButton.isVisible = false
                        }
                    }
                })
        }
}
package spacemadness.com.lunarconsole.actions

import android.content.Context
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lunar_console_layout_console_action_view.view.*
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.extensions.clearFocusAndHideKeyboard
import spacemadness.com.lunarconsole.extensions.isVisible
import spacemadness.com.lunarconsole.extensions.setPadding
import spacemadness.com.lunarconsole.reactive.filter
import spacemadness.com.lunarconsole.recyclerview.LayoutViewHolderFactory
import spacemadness.com.lunarconsole.recyclerview.RegistryListAdapter
import spacemadness.com.lunarconsole.recyclerview.ViewHolder
import spacemadness.com.lunarconsole.ui.AbstractLayout


class ActionsView(context: Context, viewModel: ActionsViewModel) : AbstractLayout(context) {
    init {
        View.inflate(context, R.layout.lunar_console_layout_console_action_view, this)

        val adapter = createListAdapter(viewModel)

        val recyclerView = recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        subscribe(viewModel.items) { adapter.submitList(it) }
    }

    private fun createListAdapter(viewModel: ActionsViewModel) =
        RegistryListAdapter().apply {
            // group
            register(ItemType.Group, createGroupViewHolderFactory())
            // actions
            register(ItemType.Action, createActionViewHolderFactory(viewModel))
            // variables
            register(ItemType.Variable, createVariableViewHolderFactory(viewModel))
        }

    //region Groups

    private fun createGroupViewHolderFactory(): LayoutViewHolderFactory {
        return LayoutViewHolderFactory(R.layout.lunar_console_layout_console_group_entry) { itemView ->
            object : ViewHolder<GroupItem>(itemView) {
                private val nameText =
                    itemView.findViewById<TextView>(R.id.lunar_console_action_group_name)

                override fun onBind(item: GroupItem, position: Int) {
                    nameText.text = item.title
                }
            }
        }
    }

    //endregion

    //region Actions

    private fun createActionViewHolderFactory(viewModel: ActionsViewModel): LayoutViewHolderFactory {
        return LayoutViewHolderFactory(R.layout.lunar_console_layout_console_action_entry) { itemView ->
            object : ViewHolder<ActionItem>(itemView) {
                private val nameText =
                    itemView.findViewById<TextView>(R.id.lunar_console_action_entry_name)

                override fun onBind(item: ActionItem, position: Int) {
                    // name
                    nameText.text = item.action.name

                    // indent items in inner groups
                    if (item.action.hasGroup) {
                        nameText.setPadding(left = 2 * nameText.paddingLeft)
                    }

                    // click listener
                    itemView.setOnClickListener {
                        viewModel.runAction(item.action.id)
                    }
                }
            }
        }
    }

    //endregion

    //region Variables

    private fun createVariableViewHolderFactory(viewModel: ActionsViewModel) =
        LayoutViewHolderFactory(R.layout.lunar_console_layout_console_variable_entry) { itemView ->
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

                private var variableId: Int = -1

                init {
                    // handle text actions
                    valueEdit.setOnEditorActionListener(createValueEditorActionListener())

                    // handle focus changes
                    valueEdit.setOnFocusChangeListener(createFocusChangeListener(viewModel))

                    // update boolean value
                    valueSwitch.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.updateVariable(variableId, isChecked)
                    }

                    // reset button
                    resetButton.setOnClickListener {
                        viewModel.resetVariable(variableId)
                    }

                    // save button
                    saveButton.setOnClickListener {
                        val newValue = valueEdit.text.toString()
                        viewModel.updateVariable(variableId, newValue)
                    }

                    // discard button
                    discardButton.setOnClickListener {
                        viewModel.discardVariable(variableId)
                    }

                    // update UI when the target variable changes
                    subscribe(
                        observable = viewModel.variableStream.filter { it.id == variableId },
                        observer = ::update
                    )
                }

                override fun onBind(item: VariableItem, position: Int) {
                    // setup target
                    variableId = item.id

                    // name
                    nameText.text = item.name

                    val variable = item.variable
                    if (item.variable is NumericVariable<*> || item.variable is StringVariable || item.variable is EnumVariable) {
                        // hide boolean switch
                        valueSwitch.isVisible = false

                        // setup InputType to avoid invalid input
                        setInputType(variable)

                    } else if (variable is BooleanVariable) {
                        // hide value edit text
                        valueEdit.isVisible = false
                    } else {
                        // FIXME: don't crash in production - just show some error in UI
                        throw IllegalStateException("Unexpected variable: $variable")
                    }

                    // editor
                    saveButton.isVisible = false
                    discardButton.isVisible = false

                    // update value
                    update(item)
                }

                private fun update(item: VariableItem) {
                    val variable = item.variable
                    if (variable is NumericVariable<*> ||
                        variable is StringVariable ||
                        variable is EnumVariable
                    ) {
                        // set value text
                        valueEdit.setText(variable.value.toString())

                        // editor
                        saveButton.isVisible = item.editorVisible
                        discardButton.isVisible = item.editorVisible

                        if (item.editorVisible) {
                            valueEdit.requestFocus()
                            valueEdit.selectAll()
                        } else {
                            valueEdit.clearFocusAndHideKeyboard()
                        }

                    } else if (variable is BooleanVariable) {
                        // set boolean value
                        valueSwitch.isChecked = variable.value
                    } else {
                        // FIXME: don't crash in production - just show some error in UI
                        throw IllegalStateException("Unexpected variable: $variable")
                    }

                    // hide/show reset button for default values
                    resetButton.isVisible = !variable.isDefault()
                }

                private fun createFocusChangeListener(viewModel: ActionsViewModel) =
                    { v: View, hasFocus: Boolean ->
                        if (hasFocus) {
                            viewModel.startEditing(variableId)
                        } else {
                            require(v is EditText) { "Unexpected view type: $v" }
                            viewModel.endEditing(variableId, v.text.toString())
                        }
                    }

                private fun createValueEditorActionListener() =
                    { v: TextView, actionId: Int, event: KeyEvent? ->
                        if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                            val value = v.text.toString()
                            if (!viewModel.updateVariable(variableId, value)) {
                                v.error = "Invalid value"
                            }
                            true
                        } else {
                            false
                        }
                    }

                private fun setInputType(variable: Variable<*>) {
                    if (variable is NumericVariable<*>) {
                        // allow negative numbers
                        var inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

                        // allow decimal point for float numbers
                        if (variable is FloatVariable) {
                            inputType = inputType or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        }
                        valueEdit.inputType = inputType
                    }
                }
            }
        }

    //endregion
}
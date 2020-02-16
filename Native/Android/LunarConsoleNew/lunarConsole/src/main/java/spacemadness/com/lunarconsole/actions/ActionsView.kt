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
import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.extensions.clearFocusAndHideKeyboard
import spacemadness.com.lunarconsole.extensions.isVisible
import spacemadness.com.lunarconsole.extensions.setPadding
import spacemadness.com.lunarconsole.reactive.filter
import spacemadness.com.lunarconsole.recyclerview.LayoutViewHolderFactory
import spacemadness.com.lunarconsole.recyclerview.ListAdapter
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
        ListAdapter().apply {
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

                private var subscription: Disposable? = null

                override fun onBind(item: VariableItem, position: Int) {
                    val variable = item.variable

                    // value edit
                    valueEdit.setSelectAllOnFocus(true)

                    // update value
                    updateValue(variable)

                    // remove old subscription
                    subscription?.let {
                        dispose(it)
                    }

                    // update UI when the target variable changes
                    subscription = subscribe(
                        observable = viewModel.variableOpStream.filter { it.id == variable.id },
                        observer = ::handleOperation
                    )
                }

                private fun handleOperation(op: VariableOperation) {
                    when (op) {
                        is VariableOperation.Update -> updateValue(op.variable)
                        is VariableOperation.EditStart -> onEditStart()
                        is VariableOperation.EditStop -> onEditStop(op.modified)
                    }
                }

                private fun updateValue(variable: Variable<*>) {
                    if (variable is NumericVariable<*> ||
                        variable is StringVariable ||
                        variable is EnumVariable
                    ) {
                        // name
                        nameText.text = variable.name

                        // hide boolean switch
                        valueSwitch.isVisible = false

                        // set value text
                        valueEdit.setText(variable.value.toString())

                        // setup InputType to avoid invalid input
                        setInputType(variable)

                        // handle text actions
                        valueEdit.setOnEditorActionListener(createValueEditorActionListener(variable))

                        // handle focus changes
                        valueEdit.setOnFocusChangeListener(
                            createFocusChangeListener(viewModel, variable)
                        )

                        valueEdit.clearFocusAndHideKeyboard()

                    } else if (variable is BooleanVariable) {
                        // hide value edit text
                        valueEdit.isVisible = false

                        // set boolean value
                        valueSwitch.isChecked = variable.value

                        // update boolean value
                        valueSwitch.setOnCheckedChangeListener { _, isChecked ->
                            viewModel.updateVariable(variable.id, isChecked)
                        }
                    } else {
                        // FIXME: don't crash in production - just show some error in UI
                        throw IllegalStateException("Unexpected variable: $variable")
                    }

                    // hide/show reset button for default values
                    if (variable.isDefault()) {
                        resetButton.isVisible = false
                    } else {
                        resetButton.isVisible = true
                        resetButton.setOnClickListener {
                            viewModel.resetVariable(variable.id)
                        }
                    }

                    // hide control buttons
                    saveButton.isVisible = false
                    saveButton.setOnClickListener {
                        val newValue = valueEdit.text.toString()
                        if (variable.stringValue != newValue) {
                            viewModel.updateVariable(variable.id, newValue)
                        } else {
                            valueEdit.clearFocusAndHideKeyboard()
                        }
                    }

                    discardButton.isVisible = false
                    discardButton.setOnClickListener {
                        valueEdit.setText(variable.stringValue)
                        valueEdit.clearFocusAndHideKeyboard()
                    }
                }

                private fun onEditStart() {
                    saveButton.isVisible = true
                    discardButton.isVisible = true
                }

                private fun onEditStop(modified: Boolean) {
                    saveButton.isVisible = modified
                    discardButton.isVisible = modified
                }

                private fun createFocusChangeListener(
                    viewModel: ActionsViewModel,
                    variable: Variable<*>
                ) =
                    { v: View, hasFocus: Boolean ->
                        if (hasFocus) {
                            viewModel.startEditing(variable.id)
                        } else {
                            require(v is EditText) { "Unexpected view type: $v" }
                            viewModel.endEditing(variable.id, v.text.toString())
                        }
                    }

                private fun createValueEditorActionListener(variable: Variable<*>) =
                    { v: TextView, actionId: Int, event: KeyEvent? ->
                        if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                            val value = v.text.toString()
                            if (!viewModel.updateVariable(variable.id, value)) {
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
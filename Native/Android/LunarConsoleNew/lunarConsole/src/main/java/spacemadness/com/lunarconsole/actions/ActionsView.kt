package spacemadness.com.lunarconsole.actions

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import spacemadness.com.lunarconsole.extensions.isVisible
import spacemadness.com.lunarconsole.extensions.setPadding
import spacemadness.com.lunarconsole.log.Log
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

        viewModel.items.subscribe { adapter.submitList(it) }
    }

    private fun createListAdapter(viewModel: ActionsViewModel) =
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
                            itemView.setOnClickListener {
                                viewModel.runAction(item.action.id)
                            }
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

                        private var subscription: Disposable? = null

                        override fun onBind(item: VariableItem, position: Int) {
                            val variable = item.variable

                            // name
                            nameText.text = variable.name

                            // value
                            valueEdit.setSelectAllOnFocus(true)
                            updateValue(variable)

                            // control buttons
                            saveButton.isVisible = false
                            discardButton.isVisible = false

                            // remove old subscription
                            subscription?.dispose()

                            // update UI when
                            subscription = viewModel
                                .variableStream
                                .filter { it.id == variable.id }
                                .subscribe(::updateValue)
                        }

                        private fun updateValue(variable: Variable<*>) {
                            if (variable is NumericVariable<*> ||
                                variable is StringVariable ||
                                variable is EnumVariable
                            ) {
                                if (variable is NumericVariable<*>) {
                                    var inputType =
                                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                                    if (variable is FloatVariable) {
                                        inputType = inputType or InputType.TYPE_NUMBER_FLAG_DECIMAL
                                    }
                                    valueEdit.inputType = inputType
                                }

                                valueEdit.setText(variable.value.toString())
                                valueEdit.addTextChangedListener(object : TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        Log.i("Text changed: $s")
                                    }
                                })
                                valueEdit.setOnEditorActionListener { v, actionId, event ->
                                    if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                                        val value = v.text.toString()
                                        if (variable.isValid(value)) {
                                            when (variable) {
                                                is StringVariable -> viewModel.updateVariable(variable, value)
                                                is IntVariable -> viewModel.updateVariable(variable, value.toInt())
                                                is FloatVariable -> viewModel.updateVariable(variable, value.toFloat())
                                                is EnumVariable -> viewModel.updateVariable(variable, value)
                                                else -> throw IllegalArgumentException("Illegal variable type: ${variable.javaClass}")
                                            }
                                            false
                                        } else {
                                            v.error = "Invalid value"
                                            true
                                        }
                                    } else {
                                        false
                                    }
                                }
                                valueSwitch.isVisible = false
                            } else if (variable is BooleanVariable) {
                                valueEdit.isVisible = false
                                valueSwitch.isChecked = variable.value
                                valueSwitch.setOnCheckedChangeListener { _, isChecked ->
                                    viewModel.updateVariable(variable, isChecked)
                                }
                            } else {
                                // FIXME: don't crash in production - just show some error in UI
                                throw IllegalStateException("Unexpected variable: $variable")
                            }

                            // default value
                            if (variable.isDefault()) {
                                resetButton.isVisible = false
                            } else {
                                resetButton.isVisible = true
                                resetButton.setOnClickListener {
                                    viewModel.resetVariable(variable.id)
                                }
                            }
                        }
                    }
                })
        }
}
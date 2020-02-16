package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.*

class ActionsViewModel(
    actions: ActionRegistry,
    private val actionRunner: ActionRunner,
    private val variables: VariableRegistry
) {
    val items =
        combineLatest(actions.itemsStream, variables.itemsStream) { result ->
            val output = mutableListOf<ListItem>()

            // actions
            addListItems("Actions", result[0], output)

            // variables
            addListItems("Variables", result[1], output)

            // result
            output as List<ListItem>
        }

    private val variableOpSubject = PublishSubject<VariableOperation>()

    val variableOpStream: Observable<VariableOperation> = merge(
        variables.variableStream.map { VariableOperation.Update(it) }, // update operations
        variableOpSubject // all other operations
    )

    fun runAction(id: ItemId) {
        actionRunner.runAction(id)
    }

    fun resetVariable(id: ItemId) {
        variables.resetVariable(id)
    }

    fun updateVariable(id: ItemId, value: String) : Boolean {
        val variable = findVariable(id)
        if (variable.isValid(value)) {
            when (variable) {
                is StringVariable -> variables.updateVariable(variable, value)
                is IntVariable -> variables.updateVariable(variable, value.toInt())
                is FloatVariable -> variables.updateVariable(variable, value.toFloat())
                is EnumVariable -> variables.updateVariable(variable, value)
                else -> throw IllegalArgumentException("Unexpected variable: ${variable.javaClass}")
            }
            return true
        }
        return false
    }

    fun updateVariable(id: ItemId, value: Boolean) {
        val variable = findVariable(id)
        require(variable is BooleanVariable)
        variables.updateVariable(variable, value)
    }

    fun startEditing(variableId: ItemId) {
        val variable = findVariable(variableId)
        variableOpSubject.post(VariableOperation.EditStart(variable))
    }

    fun endEditing(variableId: ItemId, value: String) {
        val variable = findVariable(variableId)
        val modified = value != variable.stringValue
        variableOpSubject.post(VariableOperation.EditStop(variable, modified))
    }

    private fun addListItems(title: String, items: List<Item>, output: MutableList<ListItem>) {
        if (items.isEmpty()) {
            return
        }

        output.add(GroupItem(title, collapsed = false))

        val groups = LinkedHashMap<String, MutableList<ListItem>>()
        items.forEach { item ->
            val group = item.group ?: ""
            var groupList = groups[group]
            if (groupList == null) {
                groupList = mutableListOf()
                groups[group] = groupList
            }
            val listItem = createListItem(item, index = groupList.size)
            groupList.add(listItem)
        }

        groups.forEach { (group, list) ->
            if (group.isNotEmpty()) {
                output.add(GroupItem(group, collapsed = false))
            }
            output.addAll(list)
        }
    }

    private fun createListItem(item: Item, index: Int) = when (item) {
        is Action -> ActionItem(item, index)
        is Variable<*> -> VariableItem(item, index)
        else -> throw IllegalArgumentException("Unexpected item: $item")
    }

    private fun findVariable(id: ItemId) = variables.find(id)
        ?: throw IllegalArgumentException("Variable not found: $id")
}

sealed class VariableOperation(val variable: Variable<*>) {
    val id = variable.id

    class Update(variable: Variable<*>) : VariableOperation(variable)
    class EditStart(variable: Variable<*>) : VariableOperation(variable)
    class EditStop(variable: Variable<*>, val modified: Boolean) : VariableOperation(variable)
}
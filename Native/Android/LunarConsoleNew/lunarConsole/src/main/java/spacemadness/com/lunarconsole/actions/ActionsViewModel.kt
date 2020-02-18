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

    private val editorSubject = PublishSubject<EditOperation>()
    val editorStream : Observable<EditOperation> = editorSubject

    val variableStream = variables.variableStream.map { VariableItem(it) }

    fun runAction(id: ItemId) {
        actionRunner.runAction(id)
    }

    fun resetVariable(id: ItemId) {
        variables.resetVariable(id)
    }

    fun updateVariable(id: ItemId, value: String): Boolean {
        val variable = findVariable(id)
        if (variable.isValid(value)) {
            val updated = when (variable) {
                is StringVariable -> variables.updateVariable(variable, value)
                is IntVariable -> variables.updateVariable(variable, value.toInt())
                is FloatVariable -> variables.updateVariable(variable, value.toFloat())
                is EnumVariable -> variables.updateVariable(variable, value)
                else -> throw IllegalArgumentException("Unexpected variable: ${variable.javaClass}")
            }
            // stop editing if variable value was changed
            endEditing(id, apply = true)

            return updated
        }
        return false
    }

    fun updateVariable(id: ItemId, value: Boolean) {
        val variable = findVariable(id)
        require(variable is BooleanVariable)
        variables.updateVariable(variable, value)
    }

    fun startEditing(variableId: ItemId) {
        editorSubject.post(EditOperation.Start(variableId))
    }

    fun endEditing(variableId: ItemId, apply: Boolean) {
        if (apply) {
            editorSubject.post(EditOperation.Commit(variableId))
        } else {
            val variable = findVariable(variableId)
            editorSubject.post(EditOperation.Discard(variableId, variable.stringValue))
        }
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
            groupList.add(createListItem(item))
        }

        groups.forEach { (group, list) ->
            if (group.isNotEmpty()) {
                output.add(GroupItem(group, collapsed = false))
            }
            output.addAll(list)
        }
    }

    private fun createListItem(item: Item) = when (item) {
        is Action -> ActionItem(item)
        is Variable<*> -> VariableItem(item)
        else -> throw IllegalArgumentException("Unexpected item: $item")
    }

    private fun findVariable(id: ItemId) = variables.find(id)
        ?: throw IllegalArgumentException("Variable not found: $id")
}

sealed class EditOperation(val id: ItemId) {
    class Start(id: ItemId) : EditOperation(id)
    class Commit(id: ItemId) : EditOperation(id)
    class Discard(id: ItemId, val oldValue: String) : EditOperation(id)
}
package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.reactive.combineLatest

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

    val variableStream: Observable<Variable<*>> = variables.variableStream

    fun runAction(id: ItemId) {
        actionRunner.runAction(id)
    }

    fun resetVariable(id: ItemId) {
        variables.resetVariable(id)
    }

    fun updateVariable(variable: StringVariable, value: String) = variables.updateVariable(variable, value)
    fun updateVariable(variable: IntVariable, value: Int) = variables.updateVariable(variable, value)
    fun updateVariable(variable: FloatVariable, value: Float) = variables.updateVariable(variable, value)
    fun updateVariable(variable: BooleanVariable, value: Boolean) = variables.updateVariable(variable, value)
    fun updateVariable(variable: EnumVariable, value: String) = variables.updateVariable(variable, value)

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


}
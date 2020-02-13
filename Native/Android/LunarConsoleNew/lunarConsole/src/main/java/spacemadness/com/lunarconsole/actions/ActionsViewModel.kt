package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.combineLatest

class ActionsViewModel(
    actions: ActionRegistry,
    variables: VariableRegistry
) {
    val items =
        combineLatest(actions.itemsStream, variables.itemsStream) { result ->
            val items = mutableListOf<ListItem>()

            // actions
            val actionList = result[0]
            if (actionList.isNotEmpty()) {
                items.add(HeaderItem("Actions"))
                actionList.forEach { items.add(createListItem(it)) }
            }

            // variables
            val variableList = result[1]
            if (variableList.isNotEmpty()) {
                items.add(HeaderItem("Variables"))
                variableList.forEach { items.add(createListItem(it)) }
            }
            items as List<ListItem>
        }

    private fun createListItem(item: Item) = when (item) {
        is Action -> ActionItem(item)
        is Variable<*> -> VariableItem(item)
        else -> throw IllegalArgumentException("Unexpected item: $item")
    }
}
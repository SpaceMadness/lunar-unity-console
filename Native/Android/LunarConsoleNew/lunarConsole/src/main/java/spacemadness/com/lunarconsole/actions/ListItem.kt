package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.recyclerview.ListItem as ListItemLib

enum class ItemType {
    Group,
    Action,
    Variable
}

sealed class ListItem(type: ItemType) : ListItemLib() {
    override val viewType = type.ordinal
}

abstract class ListItemWithId(
    type: ItemType,
    private val itemId: ItemId
) : ListItem(type) {
    override fun isSame(newItem: ListItemLib): Boolean {
        return newItem is ListItemWithId && itemId == newItem.itemId
    }
}

data class GroupItem(val title: String, val collapsed: Boolean) : ListItem(ItemType.Group)

data class ActionItem(val action: Action, val index: Int) :
    ListItemWithId(ItemType.Action, action.id)

data class VariableItem(val variable: Variable<*>, val index: Int) :
    ListItemWithId(ItemType.Variable, variable.id)
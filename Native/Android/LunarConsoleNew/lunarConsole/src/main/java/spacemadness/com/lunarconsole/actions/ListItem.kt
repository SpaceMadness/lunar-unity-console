package spacemadness.com.lunarconsole.actions

enum class ItemType {
    Group,
    Action,
    Variable
}

abstract class ListItem(type: ItemType) :
    spacemadness.com.lunarconsole.recyclerview.ListItem() {
    override val viewType = type.ordinal
}

data class GroupItem(val title: String, val collapsed: Boolean) :
    ListItem(ItemType.Group)

data class ActionItem(val action: Action, val index: Int) : ListItem(ItemType.Action)
data class VariableItem(val variable: Variable<*>, val index: Int) : ListItem(ItemType.Variable)
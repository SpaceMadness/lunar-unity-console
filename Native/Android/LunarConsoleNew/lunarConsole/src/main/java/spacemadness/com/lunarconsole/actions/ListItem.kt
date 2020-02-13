package spacemadness.com.lunarconsole.actions

enum class ItemType {
    Header,
    Group,
    Action,
    Variable
}

abstract class ListItem(type: ItemType) :
    spacemadness.com.lunarconsole.recyclerview.ListItem() {
    override val viewType = type.ordinal
}

data class HeaderItem(val title: String) : ListItem(ItemType.Header)
data class GroupItem(val title: String, val collapsed: Boolean) : ListItem(ItemType.Group)
data class ActionItem(val action: Action) : ListItem(ItemType.Action)
data class VariableItem(val variable: Variable<*>) : ListItem(ItemType.Variable)
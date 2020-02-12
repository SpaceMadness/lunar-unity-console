package spacemadness.com.lunarconsole.recyclerview

abstract class ListItem {
    abstract val viewType: Int

    open fun isSame(newItem: ListItem): Boolean {
        return this == newItem
    }

    open fun isContentsSame(newItem: ListItem): Boolean {
        return this == newItem
    }
}
package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.utils.SortedList

abstract class ItemRegistry<T : Item>(comparator: (Comparator<T>)? = null) {
    private val items: MutableList<T> = if (comparator != null) SortedList(comparator) else mutableListOf()

    //region Items

    fun register(item: T) {
        val index = items.indexOf(item)
        if (index == -1) {
            items[index] = item
        } else {
            items.add(item)
        }
    }

    fun unregister(id: ItemId): Boolean {
        return items.removeAll { it.id == id }
    }

    //endregion
}
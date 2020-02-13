package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.BehaviorSubject
import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.utils.SortedList

abstract class ItemRegistry<T : Item>(comparator: (Comparator<T>)? = null) {
    private val items: MutableList<T> =
        if (comparator != null) SortedList(comparator) else mutableListOf()

    private val itemsSubject = BehaviorSubject<List<T>>(items)
    val itemsStream: Observable<List<T>> = itemsSubject

    //region Items

    fun register(item: T) {
        val index = items.indexOf(item)
        if (index != -1) {
            items[index] = item
        } else {
            items.add(item)
        }
        itemsSubject.value = items
    }

    fun find(id: ItemId): Item? {
        return items.find { it.id == id }
    }

    fun unregister(id: ItemId): Boolean {
        val updated = items.removeAll { it.id == id }
        if (updated) {
            itemsSubject.value = items
        }
        return updated
    }

    //endregion
}
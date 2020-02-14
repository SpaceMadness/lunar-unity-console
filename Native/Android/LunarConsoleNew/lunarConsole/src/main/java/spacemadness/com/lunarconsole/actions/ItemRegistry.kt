package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.BehaviorSubject
import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.utils.SortedList
import java.util.Comparator

abstract class ItemRegistry<T : Item>(sorted: Boolean) {
    private val items: MutableList<T> =
        if (sorted) SortedList(createComparator()) else mutableListOf()

    private val itemsSubject = BehaviorSubject<List<T>>(items)
    val itemsStream: Observable<List<T>> = itemsSubject

    //region Items

    fun register(item: T) {
        val index = indexOf(item.id)
        if (index != -1) {
            items[index] = item
        } else {
            items.add(item)
        }
        itemsSubject.value = items
    }

    fun unregister(id: ItemId): Boolean {
        val updated = items.removeAll { it.id == id }
        if (updated) {
            itemsSubject.value = items
        }
        return updated
    }

    fun find(id: ItemId) = items.find { it.id == id }

    private fun indexOf(id: ItemId) = items.indexOfFirst { it.id == id }

    //endregion

    //region Helpers

    private fun createComparator(): Comparator<T> {
        return Comparator { o1, o2 ->
            val group1 = o1.group ?: ""
            val group2 = o2.group ?: ""
            val cmp = group1.compareTo(group2)
            if (cmp != 0) cmp else o1.name.compareTo(o2.name)
        }
    }

    //endregion
}
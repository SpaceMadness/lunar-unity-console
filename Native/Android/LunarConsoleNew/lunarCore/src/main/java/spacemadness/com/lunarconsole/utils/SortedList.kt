package spacemadness.com.lunarconsole.utils

class SortedList<T : Any>(private val comparator: Comparator<T>) : MutableList<T> {
    private val list = mutableListOf<T>()

    override val size get() = list.size

    override fun contains(element: T) = list.contains(element)

    override fun containsAll(elements: Collection<T>) = list.containsAll(elements)

    override operator fun get(index: Int) = list[index]

    override fun indexOf(element: T) = list.indexOf(element)

    override fun isEmpty() = list.isEmpty()

    override fun iterator() = list.iterator()

    override fun lastIndexOf(element: T) = list.lastIndexOf(element)

    override fun add(element: T): Boolean {
        val position = list.binarySearch(element, comparator)
        val insertPosition: Int
        if (position >= 0) {
            var i = position
            while (i < list.size && comparator.compare(list[i], element) == 0) {
                i += 1
            }
            insertPosition = i
        } else {
            insertPosition = -position - 1
        }
        list.add(insertPosition, element)
        return true
    }

    override fun add(index: Int, element: T) =
        throw IllegalStateException("Insert operation might break sorted order")

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val changed = list.addAll(index, elements)
        if (changed) {
            list.sortWith(comparator)
        }
        return changed
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val changed = list.addAll(elements)
        if (changed) {
            list.sortWith(comparator)
        }
        return changed
    }

    override fun clear() = list.clear()

    override fun listIterator() = list.listIterator()

    override fun listIterator(index: Int) = list.listIterator(index)

    override fun remove(element: T) = list.remove(element)

    override fun removeAll(elements: Collection<T>) = list.removeAll(elements)

    override fun removeAt(index: Int) = list.removeAt(index)

    override fun retainAll(elements: Collection<T>) = list.retainAll(elements)

    override operator fun set(index: Int, element: T): T {
        val prev = list.set(index, element)
        list.sortWith(comparator)
        return prev
    }

    override fun subList(fromIndex: Int, toIndex: Int) = list.subList(fromIndex, toIndex)
}

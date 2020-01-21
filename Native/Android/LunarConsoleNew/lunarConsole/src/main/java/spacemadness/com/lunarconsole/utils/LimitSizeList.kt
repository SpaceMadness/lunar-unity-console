package spacemadness.com.lunarconsole.utils

import kotlin.math.max

// TODO: rename to 'TrimList'
class LimitSizeList<E>(capacity: Int, val trimSize: Int) : Iterable<E> {
    private val data = CycleArray<E>(capacity)

    // TODO: rename to 'isOverflow'
    val isOverfloating: Boolean
        get() = data.headIndex > 0 && data.realLength() == data.capacity

    val isTrimmed: Boolean
        get() = trimmedCount() > 0

    init {
        require(trimSize > 0 && trimSize <= capacity) {
            "Invalid trim size: $trimSize. Should be within range [1..$capacity]"
        }
    }

    fun objectAtIndex(index: Int): E {
        return data[data.headIndex + index]
    }

    /**
     * Add elements from the list.
     * @return number of trimmed elements (or 0 if no elements were trimmed)
     */
    fun addAll(elements: List<E>): Int {
        val trimCount = data.addAll(elements)
        if (trimCount > 0) {
            val additionalTrim = max(0, trimSize - trimCount)
            trimHead(additionalTrim)
            return trimCount + additionalTrim
        }

        return 0
    }

    // TODO: rename to 'add'
    fun addObject(e: E): Int {
        val trimCount = data.add(e)
        if (trimCount > 0) {
            val additionalTrim = max(0, trimSize - trimCount)
            trimHead(additionalTrim)
            return trimCount + additionalTrim
        }

        return 0
    }

    fun trimHead(count: Int) {
        data.trimHeadIndex(count)
    }

    fun clear() {
        data.clear()
    }

    override fun iterator(): Iterator<E> {
        return data.iterator()
    }

    // TODO: make property
    fun capacity(): Int {
        return data.capacity
    }

    // TODO: make property
    fun totalCount(): Int {
        return data.length()
    }

    // TODO: make property
    fun count(): Int {
        return data.realLength()
    }

    // TODO: make property
    fun overflowCount(): Int {
        return data.headIndex
    }

    /**
     * Returns true if the list would overflow after adding additional elements
     */
    fun willOverflow(addCount: Int): Boolean {
        return data.realLength() + addCount > data.capacity
    }

    // TODO: make property
    fun trimmedCount(): Int {
        return totalCount() - count()
    }
}
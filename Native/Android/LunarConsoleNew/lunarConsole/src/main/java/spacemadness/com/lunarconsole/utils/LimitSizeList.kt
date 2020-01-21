package spacemadness.com.lunarconsole.utils

class LimitSizeList<E>(capacity: Int, val trimSize: Int) : Iterable<E> {
    private val data: CycleArray<E> = CycleArray(capacity)

    val isOverfloating: Boolean
        get() = data.headIndex > 0 && data.realLength() == data.capacity

    val isTrimmed: Boolean
        get() = trimmedCount() > 0

    init {
        require(trimSize > 0) { "Invalid trim size: $trimSize" }
    }

    fun objectAtIndex(index: Int): E {
        return data[data.headIndex + index]
    }

    fun addObject(e: E) {
        if (willOverflow(addCount = 1)) {
            trimHead(trimSize)
        }
        data.add(e)
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
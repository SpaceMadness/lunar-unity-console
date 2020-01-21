package spacemadness.com.lunarconsole.utils

class LimitSizeList<T>(capacity: Int, val trimSize: Int) : Iterable<T> {
    private val data: CycleArray<T>

    val isOverfloating: Boolean
        get() = data.headIndex > 0 && willOverflow()

    val isTrimmed: Boolean
        get() = trimmedCount() > 0

    init {
        require(capacity >= 0) { "Illegal capacity: $capacity" }

        this.data = CycleArray(capacity)
    }

    fun objectAtIndex(index: Int): T {
        return data[data.headIndex + index]
    }

    fun addObject(obj: T) {
        if (willOverflow()) {
            trimHead(trimSize)
        }
        data.add(obj)
    }

    fun trimHead(count: Int) {
        data.trimHeadIndex(count)
    }

    fun clear() {
        data.clear()
    }

    override fun iterator(): Iterator<T> {
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

    // TODO: make property
    fun willOverflow(): Boolean {
        return data.realLength() == data.capacity
    }

    // TODO: make property
    fun trimmedCount(): Int {
        return totalCount() - count()
    }
}
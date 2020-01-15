package spacemadness.com.lunarconsole.utils

class LimitSizeList<T>(capacity: Int, val trimSize: Int) : Iterable<T> {
    private val internalArray: CycleArray<T>

    val isOverfloating: Boolean
        get() = internalArray.headIndex > 0 && willOverflow()

    val isTrimmed: Boolean
        get() = trimmedCount() > 0

    init {
        require(capacity >= 0) { "Illegal capacity: $capacity" }

        this.internalArray = CycleArray(capacity)
    }

    fun objectAtIndex(index: Int): T {
        return internalArray[internalArray.headIndex + index]
    }

    fun addObject(obj: T) {
        if (willOverflow()) {
            trimHead(trimSize)
        }
        internalArray.add(obj)
    }

    fun trimHead(count: Int) {
        internalArray.trimHeadIndex(count)
    }

    fun clear() {
        internalArray.clear()
    }

    override fun iterator(): Iterator<T> {
        return internalArray.iterator()
    }

    // TODO: make property
    fun capacity(): Int {
        return internalArray.capacity
    }

    // TODO: make property
    fun totalCount(): Int {
        return internalArray.length()
    }

    // TODO: make property
    fun count(): Int {
        return internalArray.realLength()
    }

    // TODO: make property
    fun overflowCount(): Int {
        return internalArray.headIndex
    }

    // TODO: make property
    fun willOverflow(): Boolean {
        return internalArray.realLength() == internalArray.capacity
    }

    // TODO: make property
    fun trimmedCount(): Int {
        return totalCount() - count()
    }
}
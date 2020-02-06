package spacemadness.com.lunarconsole.utils

class ConcurrentModificationList<E : Any>(initialCapacity: Int = 0) {
    protected val data = ArrayList<E?>(initialCapacity)
    protected var updating = false
    protected var removedCount = 0

    val size get() = data.size
    val isEmpty get() = data.isEmpty()

    fun add(element: E) {
        data.add(element)
    }

    fun remove(element: E): Boolean {
        val index = data.indexOf(element)
        if (index != -1) {
            if (updating) {
                data[index] = null // replace element with 'tombstone'
                ++removedCount
            } else {
                data.removeAt(index)
            }
            return true
        }
        return false
    }

    fun clear() {
        if (updating) {
            var i = 0
            while (i < data.size) {
                data[i] = null
                ++removedCount
                ++i
            }
        } else {
            data.clear()
        }
    }

    @Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE") // TODO: this should be addressed
    inline fun forEach(callback: (index: Int, element: E) -> Unit) {
        try {
            updating = true
            val size = data.size
            var i = 0
            while (i < size) {
                val e = data[i]
                if (e != null) {
                    callback(i, e)
                }
                ++i
            }
        } finally {
            updating = false

            // clean up 'tombstones'
            var i = data.size - 1
            while (removedCount > 0 && i >= 0) {
                if (data[i] == null) {
                    data.removeAt(i)
                    removedCount -= 1
                }
                --i
            }
        }
    }
}

fun <T : Any> concurrentListOf(vararg elements: T) = ConcurrentModificationList<T>().apply {
    for (element in elements) {
        add(element)
    }
}
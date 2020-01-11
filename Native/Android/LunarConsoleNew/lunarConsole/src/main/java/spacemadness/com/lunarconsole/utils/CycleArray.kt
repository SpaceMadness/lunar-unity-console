//
//  CycleArray.kt
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.utils

/**
 * "Endless" cyclic array.
 * Once the item count reaches the maximum capacity - the items which were added first are evicted.
 */
class CycleArray<E>(val capacity: Int) : Iterable<E> {
    private val data = ArrayList<E>(capacity)

    var headIndex: Int = 0
        private set

    var length: Int = 0
        private set

    init {
        require(capacity > 0) { "Invalid capacity: $capacity" }
    }

    /**
     * Adds new entry to the array and returns the "evicted" entry in case of an overflow.
     */
    fun add(e: E): E? {
        ++length

        if (data.size < capacity) {
            data.add(e)
        } else {
            val arrayIndex = toArrayIndex(length - 1)
            val oldItem = data[arrayIndex]
            data[arrayIndex] = e

            // array "overflows"
            if (length - headIndex > data.size) {
                ++headIndex
                return oldItem
            }
        }

        return null // no items were evicted
    }

    /**
     * Clears the array
     */
    fun clear() {
        data.clear()
        length = 0
        headIndex = 0
    }

    fun trimHeadIndex(trimSize: Int) {
        trimToHeadIndex(headIndex + trimSize)
    }

    fun trimToHeadIndex(trimmedHeadIndex: Int) {
        require(trimmedHeadIndex >= headIndex && trimmedHeadIndex <= length) {
            "Trimmed head index " + trimmedHeadIndex +
                    " should be between head index " + headIndex +
                    " and length " + length
        }

        headIndex = trimmedHeadIndex
    }

    operator fun get(index: Int): E {
        val arrayIndex = toArrayIndex(index)
        return data[arrayIndex]
    }

    operator fun set(index: Int, value: E) {
        val arrayIndex = toArrayIndex(index)
        data[arrayIndex] = value
    }

    private fun toArrayIndex(index: Int): Int {
        checkIndex(index)
        return index % capacity
    }

    private fun checkIndex(index: Int) {
        require(index >= headIndex && index < length) {
            "Array index $index should be between head index $headIndex and length $length"
        }
    }

    operator fun contains(element: Any): Boolean {
        var i = headIndex
        while (i < length) {
            val arrayIndex = toArrayIndex(i)
            if (data[arrayIndex] == element) {
                return true
            }
            ++i
        }

        return false
    }

    // TODO: get rid of this function
    fun length(): Int {
        return length
    }

    // TODO: turn into a property
    fun realLength(): Int {
        return length - headIndex
    }

    //region Iterable

    override fun iterator(): Iterator<E> {
        return CycleIterator(this, headIndex)
    }

    private class CycleIterator<E>(private val arr: CycleArray<E>, headIndex: Int) : Iterator<E> {
        private var index: Int = 0

        init {
            index = headIndex
        }

        override fun hasNext(): Boolean {
            return index < arr.length()
        }

        override fun next(): E {
            return arr[index++]
        }
    }

    //endregion
}
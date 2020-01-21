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

import kotlin.math.max
import kotlin.math.min

/**
 * "Endless" cyclic array
 * @param capacity - maximum number of elements the list can hold before trimming.
 */
class CycleArray<E>(val capacity: Int) : Iterable<E> {
    init {
        require(capacity > 0) { "Invalid capacity: $capacity" }
    }

    // FIXME: store nullable types in order to free items when trimming head
    private val data = ArrayList<E>(capacity)

    var headIndex: Int = 0
        private set

    var length: Int = 0
        private set

    /**
     * Add elements from the list.
     * @return number of trimmed elements (or 0 if no elements were trimmed)
     */
    fun addAll(elements: List<E>): Int {
        // add all elements if they fit into the array
        if (data.size + elements.size <= capacity) {
            length += elements.size
            data.addAll(elements)
            return 0
        }

        val copySize = min(elements.size, capacity)
        val discardSize = elements.size - copySize
        val start = (length + discardSize) % capacity
        val rightSize = min(capacity - start, copySize)
        val leftSize = copySize - rightSize
        val off = elements.size - copySize
        replaceDataRange(pos = start, elements = elements, off = off, len = rightSize)
        if (leftSize > 0) {
            replaceDataRange(pos = 0, elements = elements, off = off + rightSize, len = leftSize)
        }

        length += elements.size
        val oldHeadIndex = headIndex
        headIndex = max(oldHeadIndex, length - capacity)
        return headIndex - oldHeadIndex
    }

    /**
     * Adds new entry to the array and returns the "evicted" entry in case of an overflow.
     */
    fun add(e: E): Int {
        ++length

        if (data.size < capacity) {
            data.add(e)
            return 0
        }

        val arrayIndex = toArrayIndex(length - 1)
        data[arrayIndex] = e

        // array "overflows"
        if (length - headIndex > capacity) {
            ++headIndex
            return 1
        }

        return 0
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

    private fun replaceDataRange(pos: Int, elements: List<E>, off: Int, len: Int) {
        require(pos >= 0 && pos + len <= capacity) {
            "pos ($pos) and len ($len) should be within range [0..$capacity]"
        }
        require(off >= 0 && off + len <= elements.size) {
            "off ($off) and len ($len) should be within range [0..${elements.size}]"
        }

        var i = 0
        while (i < len && pos + i < data.size) {
            data[pos + i] = elements[off + i]
            i += 1
        }
        while (i < len) {
            data.add(elements[off + i])
            i += 1
        }
    }

    //region Iterable

    override fun iterator(): Iterator<E> {
        return CycleIterator(this, headIndex)
    }

    private class CycleIterator<E>(private val arr: CycleArray<E>, headIndex: Int) : Iterator<E> {
        private var index: Int = headIndex

        override fun hasNext(): Boolean {
            return index < arr.length
        }

        override fun next(): E {
            return arr[index++]
        }
    }

    //endregion
}

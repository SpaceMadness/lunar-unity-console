package spacemadness.com.lunarconsole.utils

fun <T : Comparable<T>> List<T>.sortedInsertionPoint(t: T): Int {
    val index = binarySearch(t)
    return if (index >= 0) index else (-index - 1)
}
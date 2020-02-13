package spacemadness.com.lunarconsole.utils

data class Range<T : Comparable<T>>(val start: T, val end: T) {
    init {
        require(start <= end) { "Invalid range: [$start..$end]" }
    }
}
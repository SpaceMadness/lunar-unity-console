package spacemadness.com.lunarconsole.model.log

interface LogEntryLookup {
    fun addEntry(entry: LogEntry): CollapsedLogEntry
    fun clear()
}

internal class LogEntryHashTableLookup : LogEntryLookup {
    private val lookup = mutableMapOf<String, CollapsedLogEntry>()

    override fun addEntry(entry: LogEntry): CollapsedLogEntry {
        val existingCollapsedEntry = lookup[entry.message]
        if (existingCollapsedEntry != null) {
            existingCollapsedEntry.increaseCount()
            return existingCollapsedEntry
        }

        val collapsedLogEntry = entry.asCollapsedEntry()
        lookup[entry.message] = collapsedLogEntry
        return collapsedLogEntry
    }

    override fun clear() {
        lookup.clear()
    }
}
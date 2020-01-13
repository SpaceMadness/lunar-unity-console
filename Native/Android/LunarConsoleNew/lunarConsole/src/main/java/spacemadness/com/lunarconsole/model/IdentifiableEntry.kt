package spacemadness.com.lunarconsole.model

typealias EntryId = Int

open class IdentifiableEntry(val id: EntryId, val name: String, val group: String? = null) : Entry
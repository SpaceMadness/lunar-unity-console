package spacemadness.com.lunarconsole.model

typealias VariableFlags = Int

class Variable<T>(
    id: EntryId,
    name: String,
    val value: T,
    val defaultValue: T,
    flags: VariableFlags,
    group: String? = null
) : IdentifiableEntry(id = id, name = name, group = group)



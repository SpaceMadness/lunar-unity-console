package spacemadness.com.lunarconsole.actions

typealias VariableFlags = Int

class Variable<T>(
    id: ItemId,
    name: String,
    val value: T,
    val defaultValue: T,
    flags: VariableFlags,
    group: String? = null
) : Item(id = id, name = name, group = group)



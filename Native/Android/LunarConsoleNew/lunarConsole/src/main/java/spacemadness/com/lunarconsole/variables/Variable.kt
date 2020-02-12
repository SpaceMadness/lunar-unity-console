package spacemadness.com.lunarconsole.variables

import spacemadness.com.lunarconsole.actions.ItemId
import spacemadness.com.lunarconsole.actions.Item

typealias VariableFlags = Int

class Variable<T>(
    id: ItemId,
    name: String,
    val value: T,
    val defaultValue: T,
    flags: VariableFlags,
    group: String? = null
) : Item(id = id, name = name, group = group)



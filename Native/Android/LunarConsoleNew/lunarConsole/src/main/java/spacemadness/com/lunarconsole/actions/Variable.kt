package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.utils.Range

typealias VariableFlags = Int

abstract class Variable<T>(
    id: ItemId,
    name: String,
    var value: T,
    val defaultValue: T,
    val flags: VariableFlags,
    group: String?
) : Item(
    id = id,
    name = name,
    group = group
) {
    fun isDefault() = value == defaultValue

    fun reset(): Boolean {
        if (isDefault()) {
            value = defaultValue
            return true
        }
        return false
    }

    override fun toString() = value.toString()
}

class StringVariable(
    id: ItemId,
    name: String,
    value: String?,
    defaultValue: String?,
    flags: VariableFlags = 0,
    group: String? = null
) : Variable<String?>(
    id = id,
    name = name,
    value = value,
    defaultValue = defaultValue,
    flags = flags,
    group = group
)

class BooleanVariable(
    id: ItemId,
    name: String,
    value: Boolean,
    defaultValue: Boolean = false,
    flags: VariableFlags = 0,
    group: String? = null
) : Variable<Boolean>(
    id = id,
    name = name,
    value = value,
    defaultValue = defaultValue,
    flags = flags,
    group = group
)

sealed class NumericVariable<T : Comparable<T>>(
    id: ItemId,
    name: String,
    value: T,
    defaultValue: T,
    flags: VariableFlags,
    val range: Range<T>?,
    group: String?
) : Variable<T>(
    id = id,
    name = name,
    value = value,
    defaultValue = defaultValue,
    flags = flags,
    group = group
) {
    val hasRange = range != null
}

class IntVariable(
    id: ItemId,
    name: String,
    value: Int,
    defaultValue: Int = 0,
    flags: VariableFlags = 0,
    range: Range<Int>? = null,
    group: String? = null
) : NumericVariable<Int>(
    id = id,
    name = name,
    value = value,
    defaultValue = defaultValue,
    flags = flags,
    range = range,
    group = group
)

class FloatVariable(
    id: ItemId,
    name: String,
    value: Float,
    defaultValue: Float = 0.0f,
    flags: VariableFlags = 0,
    range: Range<Float>? = null,
    group: String? = null
) : NumericVariable<Float>(
    id = id,
    name = name,
    value = value,
    defaultValue = defaultValue,
    flags = flags,
    range = range,
    group = group
)

class EnumVariable(
    id: ItemId,
    name: String,
    value: String,
    defaultValue: String,
    val values: List<String>,
    flags: VariableFlags = 0,
    group: String? = null
) : Variable<String>(
    id = id,
    name = name,
    value = value,
    defaultValue = defaultValue,
    flags = flags,
    group = group
)


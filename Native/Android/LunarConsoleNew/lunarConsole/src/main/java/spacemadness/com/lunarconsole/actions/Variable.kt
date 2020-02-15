package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.utils.Range

typealias VariableFlags = Int

abstract class Variable<T>(
    id: ItemId,
    name: String,
    val value: T,
    val defaultValue: T,
    val flags: VariableFlags,
    group: String?
) : Item(id = id, name = name, group = group) {
    fun isDefault() = value == defaultValue

    fun reset() = update(defaultValue)

    fun update(value: T): Variable<T> {
        if (this.value != value) {
            return copy(value)
        }
        return this
    }

    protected abstract fun copy(value: T): Variable<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Variable<*>

        if (value != other.value) return false
        if (defaultValue != other.defaultValue) return false
        if (flags != other.flags) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (defaultValue?.hashCode() ?: 0)
        result = 31 * result + flags
        return result
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
) {
    override fun copy(value: String?) = StringVariable(
        id = id,
        name = name,
        value = value,
        defaultValue = defaultValue,
        flags = flags,
        group = group
    )
}

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
) {
    override fun copy(value: Boolean) = BooleanVariable(
        id = id,
        name = name,
        value = value,
        defaultValue = defaultValue,
        flags = flags,
        group = group
    )
}

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as NumericVariable<*>

        if (range != other.range) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (range?.hashCode() ?: 0)
        return result
    }
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
) {
    override fun copy(value: Int) = IntVariable(
        id = id,
        name = name,
        value = value,
        defaultValue = defaultValue,
        flags = flags,
        range = range,
        group = group
    )
}

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
) {
    override fun copy(value: Float) = FloatVariable(
        id = id,
        name = name,
        value = value,
        defaultValue = defaultValue,
        flags = flags,
        range = range,
        group = group
    )
}

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
) {
    override fun copy(value: String) = EnumVariable(
        id = id,
        name = name,
        value = value,
        defaultValue = defaultValue,
        values = values,
        flags = flags,
        group = group
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as EnumVariable

        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }
}


package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.reactive.PublishSubject

class VariableRegistry(sorted: Boolean) : ItemRegistry<Variable<*>>(sorted) {
    private val variableSubject = PublishSubject<Variable<*>>()
    val variableStream: Observable<Variable<*>> = variableSubject

    fun resetVariable(id: ItemId) {
        val variable = find(id) ?: throw IllegalArgumentException("Variable not registered: $id")
        if (!variable.isDefault()) {
            updateVariable(id, variable.defaultValue)
        }
    }

    fun updateVariable(variable: StringVariable, value: String) = updateVariable(variable.id, value)
    fun updateVariable(variable: IntVariable, value: Int) = updateVariable(variable.id, value)
    fun updateVariable(variable: FloatVariable, value: Float) = updateVariable(variable.id, value)
    fun updateVariable(variable: BooleanVariable, value: Boolean) = updateVariable(variable.id, value)
    fun updateVariable(variable: EnumVariable, value: String) = updateVariable(variable.id, value)

    private fun <T> updateVariable(id: ItemId, value: T): Boolean {
        // find variable
        val variable = find(id) ?: throw IllegalArgumentException("Variable not registered: $id")

        // check type
        @Suppress("UNCHECKED_CAST")
        variable as? Variable<T>
            ?: throw IllegalArgumentException("Invalid variable value type: ${variable.javaClass}")

        if (value != variable.value) {
            val newVariable = variable.update(value)
            update(id, newVariable)
            variableSubject.post(newVariable)
            return true
        }

        return false
    }
}
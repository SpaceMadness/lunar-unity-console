package spacemadness.com.lunarconsole.actions

interface VariableEditor {
    fun resetVariable(id: ItemId)
    fun <T> updateVariable(variable: Variable<T>, value: T)
}

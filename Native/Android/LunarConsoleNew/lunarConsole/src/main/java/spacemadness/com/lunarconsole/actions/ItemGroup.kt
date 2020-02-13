package spacemadness.com.lunarconsole.actions

data class ItemGroup<T : Item>(
    val id: ItemId,
    val name: String,
    val items: List<T> = emptyList(),
    val isCollapsed: Boolean = false
) : Iterable<T> {
    val size = items.size
    val isEmpty = items.isEmpty()

    fun collapse() = collapsed(true)
    fun expand() = collapsed(false)
    fun collapsed(collapsed: Boolean) =
        if (isCollapsed != collapsed) copy(isCollapsed = collapsed) else this

    override fun iterator() = items.iterator()
}
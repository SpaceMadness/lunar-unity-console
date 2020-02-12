package spacemadness.com.lunarconsole.actions

class Action(id: ItemId, name: String, group: String? = null) :
    Item(id = id, name = name, group = group), Comparable<Action> {
    override fun compareTo(other: Action): Int {
        val group1 = group ?: ""
        val group2 = other.group ?: ""

        val cmp = group1.compareTo(group2)
        return if (cmp == 0) name.compareTo(other.name) else cmp
    }
}
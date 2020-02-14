package spacemadness.com.lunarconsole.actions

typealias ItemId = Int

abstract class Item(
    val id: ItemId,
    val name: String,
    val group: String? = null
) {
    val hasGroup = !group.isNullOrEmpty()
}
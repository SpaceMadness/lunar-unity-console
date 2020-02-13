package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.reactive.map

class ActionsViewModel(
    actions: ActionRegistry
) {
    val items: Observable<List<ListItem>> = actions.itemsStream.map { actions ->
        actions.map { action -> ActionItem(action) }
    }
}
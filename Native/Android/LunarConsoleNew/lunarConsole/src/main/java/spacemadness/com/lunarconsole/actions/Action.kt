package spacemadness.com.lunarconsole.actions

import spacemadness.com.lunarconsole.model.EntryId
import spacemadness.com.lunarconsole.model.IdentifiableEntry

class Action(id: EntryId, name: String, group: String? = null) :
    IdentifiableEntry(id = id, name = name, group = group)
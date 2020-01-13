package spacemadness.com.lunarconsole.model

class Action(id: EntryId, name: String, group: String? = null) :
    IdentifiableEntry(id = id, name = name, group = group)
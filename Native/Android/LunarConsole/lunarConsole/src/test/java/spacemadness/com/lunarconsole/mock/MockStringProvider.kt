package spacemadness.com.lunarconsole.mock

import spacemadness.com.lunarconsole.core.StringProvider

class MockStringProvider : StringProvider {
    private val lookup = mutableMapOf<Int, String>()

    fun add(id: Int, value: String) {
        lookup[id] = value
    }

    override fun getString(id: Int): String {
        return lookup[id] ?: throw IllegalArgumentException("Id not found: $id")
    }
}
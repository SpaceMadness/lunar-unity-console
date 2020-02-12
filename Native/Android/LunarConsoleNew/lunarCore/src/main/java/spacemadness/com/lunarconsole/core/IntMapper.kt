package spacemadness.com.lunarconsole.core

interface IntMapper<in T> {
    fun map(t: T): Int

    operator fun invoke(t: T): Int {
        return map(t)
    }
}
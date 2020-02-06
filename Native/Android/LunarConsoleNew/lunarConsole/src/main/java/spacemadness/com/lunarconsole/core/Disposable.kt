package spacemadness.com.lunarconsole.core

/**
 * Represents a disposable resource.
 */
interface Disposable {
    /**
     * Dispose the resource, the operation should be idempotent.
     */
    fun dispose()
}
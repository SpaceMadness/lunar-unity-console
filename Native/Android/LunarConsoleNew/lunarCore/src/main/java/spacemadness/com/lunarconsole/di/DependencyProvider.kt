package spacemadness.com.lunarconsole.di

import androidx.annotation.VisibleForTesting

/**
 * Service Locator design pattern implementation. Provides a lazy dependency injection resolution
 * based on the target class.
 */
object DependencyProvider {
    /**
     * Provider lookup by target class.
     */
    private val lookup = mutableMapOf<Class<*>, Provider<*>>()

    /**
     * Registers new [Provider] for a specified class.
     */
    inline fun <reified T : Any> register(provider: Provider<T>) {
        register(T::class.java, provider)
    }

    /**
     * Registers new target object for a specified class.
     */
    inline fun <reified T : Any> register(target: T) {
        register(T::class.java, object : Provider<T> {
            override fun get(): T = target
        })
    }

    /**
     * Registers new [Provider] for a specified class.
     */
    fun <T : Any> register(type: Class<T>, provider: Provider<T>) {
        lookup[type] = provider
    }

    /**
     * Returns provide associated with this target type.
     */
    inline fun <reified T : Any> of() = of(T::class.java)

    /**
     * Returns provide associated with this target type.
     */
    fun <T : Any> of(type: Class<T>): T {
        val provider = lookup[type]
            ?: throw IllegalArgumentException("Provider is not registered: $type")

        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }

    /**
     * Clears all dependency providers.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun clear() {
        lookup.clear()
    }
}
package spacemadness.com.lunarconsole.di

internal object Providers {
    private val lookup = mutableMapOf<Class<*>, Provider>()

    inline fun <reified T : Provider> of(): T = of(T::class.java)

    fun <T : Provider> of(type: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return lookup[type] as? T ?: throw IllegalStateException("Provider not registered: $type")
    }

    inline fun <reified T : Provider> register(provider: T) = register(T::class.java, provider)

    fun <T : Provider> register(type: Class<T>, provider: Provider) {
        lookup[type] = provider
    }

    fun clear() {
        lookup.clear()
    }
}
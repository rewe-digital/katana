package org.rewedigital.katana

/**
 * Unique key per dependency declaration based either on the class or name of the dependency.
 */
@PublishedApi
internal sealed class Key {

    /**
     * Optional additional key that is part of a key.
     * A context key might be set for bindings inside a specific binding context which should be unique per context.
     */
    internal abstract val contextKey: Key?

    /**
     * Optional increment which is required for multi bindings (set bindings) as a set for instance defines multiple
     * declarations of the same type.
     */
    internal abstract val increment: Int?

    data class ClassKey<T>(
        val clazz: Class<T>,
        override val contextKey: Key? = null,
        override val increment: Int? = null
    ) : Key()

    data class NameKey(
        val name: Any,
        override val contextKey: Key? = null,
        override val increment: Int? = null
    ) : Key()

    companion object {

        fun <T> of(
            clazz: Class<T>,
            name: Any? = null,
            contextKey: Key? = null,
            increment: Int? = null
        ): Key =
            when (name) {
                null -> ClassKey(clazz = clazz, contextKey = contextKey, increment = increment)
                else -> NameKey(name = name, contextKey = contextKey, increment = increment)
            }
    }
}

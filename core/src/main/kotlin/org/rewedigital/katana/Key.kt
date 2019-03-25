package org.rewedigital.katana

/**
 * Unique key per dependency declaration based either on the class or name of the dependency.
 */
@PublishedApi
internal sealed class Key {

    data class ClassKey<T>(val clazz: Class<T>) : Key()

    data class NameKey(val name: Any) : Key()

    companion object {

        fun <T> of(clazz: Class<T>, name: Any? = null): Key =
            when (name) {
                null -> forClass(clazz)
                else -> forName(name)
            }

        private fun <T> forClass(clazz: Class<T>): Key =
            ClassKey(clazz)

        private fun forName(name: Any): Key = NameKey(name)
    }
}

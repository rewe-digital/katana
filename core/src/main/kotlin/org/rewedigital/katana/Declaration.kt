package org.rewedigital.katana

/**
 * Represents a dependency declaration.
 */
data class Declaration<T>(val key: Key,
                          val type: Type,
                          val moduleName: String?,
                          val clazz: Class<T>,
                          val name: String?,
                          val provider: Provider<T>) {

    enum class Type { FACTORY, SINGLETON, EAGER_SINGLETON }

    override fun toString() =
        "${clazz.name}(type=$type${name?.let { ", name=$it" }.orEmpty()}${moduleName?.let { ", module=$it" }.orEmpty()})"
}

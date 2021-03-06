package org.rewedigital.katana

/**
 * Represents a dependency declaration.
 */
@PublishedApi
internal data class Declaration<T>(
    val key: Key,
    val type: Type,
    val moduleId: Int,
    val moduleName: String?,
    val clazz: Class<T>,
    val name: Any?,
    val provider: Provider<T>,
    val internal: Boolean
) {

    enum class Type(
        val permitsRedeclaration: Boolean = false
    ) {
        FACTORY,
        SINGLETON,
        EAGER_SINGLETON,
        SET(permitsRedeclaration = true),
        CUSTOM
    }

    override fun toString() =
        "${clazz.name}(type=$type${name?.let { ", name=$it" }.orEmpty()}${moduleName?.let { ", module=$it" }.orEmpty()})"
}

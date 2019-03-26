package org.rewedigital.katana

import org.rewedigital.katana.dsl.ModuleDslMarker
import java.util.concurrent.atomic.AtomicInteger

/**
 * Defines a module (with an optional name).
 *
 * @param name Optional name. Is used for exception messages and may improve debugging DI-related issues.
 * @param includes Collection of modules included by this module.
 *
 * @see Module
 */
fun createModule(
    name: String? = null,
    includes: Iterable<Module> = emptyList(),
    body: Module.() -> Unit = {}
) =
    Module(
        id = ModuleIdProvider.id,
        name = name,
        includes = includes
    ).apply {
        body.invoke(this)
    }

/**
 * A module consists of dependency declarations and describes how dependencies are provided.
 * Each module should represent a logical unit. For instance there should be separate modules for each functionality
 * of your application.
 *
 * @param name Optional name. Is used for exception messages and may improve debugging DI-related issues.
 * @param includes Collection of modules included by this module.
 *
 * @see createModule
 * @see Component
 */
@ModuleDslMarker
class Module internal constructor(
    internal val id: Int,
    internal val name: String?,
    internal val includes: Iterable<Module>
) {

    internal val declarations = Katana.environmentContext.mapFactory().create<Key, Declaration<*>>()
}

internal object ModuleIdProvider {

    private val nextId = AtomicInteger()

    val id
        get() = nextId.getAndIncrement()
}

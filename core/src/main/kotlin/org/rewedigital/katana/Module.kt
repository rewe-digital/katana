package org.rewedigital.katana

import java.util.concurrent.atomic.AtomicInteger

/**
 * A module consists of dependency declarations and describes how dependencies are provided.
 * Each module should represent a logical unit. For instance there should be separate modules for each functionality
 * of your application.
 *
 * @param name Optional name. Is used for exception messages and may improve debugging DI-related issues.
 * @param includes Collection of modules included by this module.
 *
 * @see Component
 */
class Module private constructor(
    internal val id: Int,
    internal val name: String?,
    internal val includes: Iterable<Module>
) {
    private val bindingContext = ModuleBindingContext(this)

    /**
     * Creates a module (with an optional name).
     *
     * @param name Optional name. Is used for exception messages and may improve debugging DI-related issues.
     * @param includes Collection of modules included by this module.
     *
     * @see Module
     */
    constructor(
        name: String? = null,
        includes: Iterable<Module> = emptyList(),
        declarations: ModuleBindingContext.() -> Unit = {}
    ) : this(
        id = ModuleIdProvider.id,
        name = name,
        includes = includes
    ) {
        declarations.invoke(bindingContext)
    }

    internal val declarations = Katana.environmentContext.mapFactory().create<Key, Declaration<*>>()
}

private object ModuleIdProvider {

    private val nextId = AtomicInteger()

    val id
        get() = nextId.getAndIncrement()
}

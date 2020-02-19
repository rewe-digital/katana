package org.rewedigital.katana

import java.util.concurrent.atomic.AtomicInteger

/**
 * Describes **how** dependencies are resolved by providing dependency declarations via `factory`, `singleton`, etc.
 *
 * A module is not responsible for holding instances of dependencies or for performing actual injection. It can be
 * interpreted as a blueprint of dependency declarations which is applied to a [Component]. Therefore one module might
 * be referenced by multiple components.
 *
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

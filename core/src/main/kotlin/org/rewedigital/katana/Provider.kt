package org.rewedigital.katana

import org.rewedigital.katana.dsl.ProviderDsl

/**
 * Describes how a dependency is provided
 */
interface Provider<T> {

    /**
     * The [ComponentContext] of the current context is passed to the function which enables transitive dependency
     * injection, e.g. injection of dependencies within a module.
     */
    operator fun invoke(context: ComponentContext, arg: Any? = null): T
}

@PublishedApi
internal class DefaultProvider<T>(
    private val body: ProviderDsl.() -> T
) : Provider<T> {

    override operator fun invoke(context: ComponentContext, arg: Any?): T =
        body.invoke(ProviderDsl(context))
}

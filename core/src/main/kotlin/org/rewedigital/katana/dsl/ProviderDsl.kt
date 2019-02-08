package org.rewedigital.katana.dsl

import org.rewedigital.katana.ComponentContext

/**
 * Provides syntax for injecting transitive dependencies via [get] within the provider body.
 *
 * @see get
 * @see lazy
 */
@ModuleDslMarker
class ProviderDsl(val context: ComponentContext)

/**
 * Provides a dependency which has already been declared in the current context (total set of modules of the
 * current component) to be able to inject transitive dependencies within a module.
 */
inline fun <reified T> ProviderDsl.get(name: String? = null) =
    context.injectNow<T>(name = name, internal = true)

/**
 * Provides a [Lazy] version of dependency. Should only be required to circumvent a circular dependency cycle.
 * Better solution is to structure classes in a way that circular dependencies are not necessary.
 */
inline fun <reified T> ProviderDsl.lazy(name: String? = null) =
    lazy { get<T>(name) }
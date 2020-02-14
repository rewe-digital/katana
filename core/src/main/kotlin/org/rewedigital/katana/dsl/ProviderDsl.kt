package org.rewedigital.katana.dsl

import org.rewedigital.katana.Component
import org.rewedigital.katana.ComponentContext
import org.rewedigital.katana.InjectionException

/**
 * Provides syntax for injecting transitive dependencies via [get], [getOrNull] or [provider] within the provider body.
 *
 * @see get
 * @see getOrNull
 * @see provider
 */
@ModuleDslMarker
class ProviderDsl(val context: ComponentContext)

/**
 * Provides a dependency which is declared in the current context (total set of modules of the
 * current component). Allows injection of transitive dependencies within a module.
 *
 * @param name Optional name of binding
 * @return Requested dependency
 * @throws InjectionException if no binding for dependency was found
 * @see getOrNull
 * @see provider
 */
inline fun <reified T> ProviderDsl.get(name: Any? = null) =
    context.injectNow<T>(name = name, internal = true)

/**
 * Provides an optional dependency which might be declared in the current context.
 *
 * @param name Optional name of binding
 * @return Requested dependency or `null` if no binding for dependency was found
 * @see get
 */
inline fun <reified T> ProviderDsl.getOrNull(name: Any? = null) =
    context.injectNowOrNull<T>(name = name, internal = true)

/**
 * Returns a function that when invoked provides a (new) instance of requested dependency.
 *
 * For dependencies declared as [singleton] the function will return the **same** instance on every invocation!
 * Basically this is [get] wrapped in a lambda.
 *
 * @param name Optional name of binding
 * @throws InjectionException on invocation of provider function if no binding for dependency was found
 * @see get
 */
inline fun <reified T> ProviderDsl.provider(name: Any? = null): () -> T = { get(name) }

/**
 * Special property for providing the [Component] that this [Module] is associated with.
 */
val ProviderDsl.component: Component
    get() = context.thisComponent

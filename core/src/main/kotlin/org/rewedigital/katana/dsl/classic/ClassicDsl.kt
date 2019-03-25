package org.rewedigital.katana.dsl.classic

import org.rewedigital.katana.Component
import org.rewedigital.katana.Declaration
import org.rewedigital.katana.Declaration.Type
import org.rewedigital.katana.Module
import org.rewedigital.katana.Provider
import org.rewedigital.katana.dsl.ModuleDslMarker
import org.rewedigital.katana.dsl.ProviderDsl
import org.rewedigital.katana.dsl.internal.moduleDeclaration

/**
 * Declares a dependency binding.
 *
 * @param name Optional name of binding. See documentation of package [org.rewedigital.katana] for more details.
 * @param internal If `true` binding is only available in current module
 * @param body Body of binding declaration
 *
 * @see factory
 * @see singleton
 * @see eagerSingleton
 */
inline fun <reified T> Module.bind(name: Any? = null,
                                   internal: Boolean = false,
                                   body: BindingDsl<T>.() -> Module) =
    body.invoke(BindingDsl(module = this,
                           clazz = T::class.java,
                           name = name,
                           internal = internal))

/**
 * Provides syntax for declaring how the dependency is provided, either via [factory], [singleton] or [eagerSingleton].
 */
@ModuleDslMarker
class BindingDsl<T>(private val module: Module,
                    private val clazz: Class<T>,
                    private val name: Any?,
                    private val internal: Boolean) {

    internal fun declaration(type: Declaration.Type,
                             provider: Provider<T>) =
        moduleDeclaration(
            module = module,
            clazz = clazz,
            name = name,
            internal = internal,
            type = type,
            provider = provider
        )
}

/**
 * Provides the dependency via a factory. A new instance will be created every time the dependency is requested.
 */
fun <T> BindingDsl<T>.factory(body: ProviderDsl.() -> T): Module =
    declaration(Type.FACTORY) { context -> body.invoke(ProviderDsl(context)) }

/**
 * Provides the dependency as a singleton. Only one instance (per component) will be created.
 */
fun <T> BindingDsl<T>.singleton(body: ProviderDsl.() -> T): Module =
    declaration(Type.SINGLETON) { context -> body.invoke(ProviderDsl(context)) }

/**
 * Provides the dependency as an eager singleton. Only once instance (per component) will be created.
 * The instance will be created when the [Component] is created and not lazily the first time it's requested.
 */
fun <T> BindingDsl<T>.eagerSingleton(body: ProviderDsl.() -> T): Module =
    declaration(Type.EAGER_SINGLETON) { context -> body.invoke(ProviderDsl(context)) }

package org.rewedigital.katana.dsl.compact

import org.rewedigital.katana.Component
import org.rewedigital.katana.Declaration.Type
import org.rewedigital.katana.DefaultProvider
import org.rewedigital.katana.Module
import org.rewedigital.katana.Provider
import org.rewedigital.katana.dsl.ProviderDsl
import org.rewedigital.katana.dsl.internal.moduleDeclaration

/**
 * Declares a dependency binding.
 * A new instance will be created every time the dependency is requested.
 *
 * @param name Optional name of binding. See documentation of package [org.rewedigital.katana] for more details.
 * @param internal If `true` binding is only available in current module
 * @param body Body of binding declaration
 *
 * @see Module.singleton
 * @see Module.eagerSingleton
 */
inline fun <reified T> Module.factory(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    moduleDeclaration(
        module = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Type.FACTORY,
        provider = DefaultProvider(body)
    )

/**
 * Declares a dependency binding as a singleton.
 * Only one instance (per component) will be created.
 *
 * @param name Optional name of binding. See documentation of package [org.rewedigital.katana] for more details.
 * @param internal If `true` binding is only available in current module
 * @param body Body of binding declaration
 *
 * @see Module.factory
 * @see Module.eagerSingleton
 */
inline fun <reified T> Module.singleton(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    moduleDeclaration(
        module = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Type.SINGLETON,
        provider = DefaultProvider(body)
    )

/**
 * Declares a dependency binding as a eager singleton.
 * Only once instance (per component) will be created.
 * The instance will be created when the [Component] is created and not lazily the first time it's requested.
 *
 * @param name Optional name of binding. See documentation of package [org.rewedigital.katana] for more details.
 * @param internal If `true` binding is only available in current module
 * @param body Body of binding declaration
 *
 * @see Module.factory
 * @see Module.singleton
 */
inline fun <reified T> Module.eagerSingleton(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    moduleDeclaration(
        module = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Type.EAGER_SINGLETON,
        provider = DefaultProvider(body)
    )

/**
 * Declares a custom binding with a custom implementation of [Provider].
 *
 * A custom provider might for instance handle additional arguments passed to [Provider.invoke].
 * This should rarely be used and is rather meant for Katana extensions!
 *
 * @see Component.custom
 */
inline fun <reified T> Module.custom(
    name: Any? = null,
    internal: Boolean = false,
    provider: Provider<T>
) =
    moduleDeclaration(
        module = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Type.CUSTOM,
        provider = provider
    )

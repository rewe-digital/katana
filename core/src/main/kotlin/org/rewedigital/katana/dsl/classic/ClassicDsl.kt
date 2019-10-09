package org.rewedigital.katana.dsl.classic

import org.rewedigital.katana.Component
import org.rewedigital.katana.Declaration.Type
import org.rewedigital.katana.DefaultProvider
import org.rewedigital.katana.ModuleBindingContext
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
@Deprecated(
    message = "Use new DSL syntax under org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("")
)
inline fun <reified T> ModuleBindingContext.bind(
    name: Any? = null,
    internal: Boolean = false,
    body: BindingDsl<T>.() -> ModuleBindingContext
) =
    body.invoke(
        BindingDsl(
            context = this,
            clazz = T::class.java,
            name = name,
            internal = internal
        )
    )

/**
 * Provides syntax for declaring how the dependency is provided, either via [factory], [singleton] or [eagerSingleton].
 */
@ModuleDslMarker
class BindingDsl<T>(
    private val context: ModuleBindingContext,
    private val clazz: Class<T>,
    private val name: Any?,
    private val internal: Boolean
) {

    internal fun declaration(
        type: Type,
        body: ProviderDsl.() -> T
    ) =
        moduleDeclaration(
            context = context,
            clazz = clazz,
            name = name,
            internal = internal,
            type = type,
            provider = DefaultProvider(body)
        )
}

/**
 * Provides the dependency via a factory. A new instance will be created every time the dependency is requested.
 */
@Deprecated(
    message = "Use new DSL syntax under org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("")
)
fun <T> BindingDsl<T>.factory(body: ProviderDsl.() -> T): ModuleBindingContext =
    declaration(Type.FACTORY, body)

/**
 * Provides the dependency as a singleton. Only one instance (per component) will be created.
 */
@Deprecated(
    message = "Use new DSL syntax under org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("")
)
fun <T> BindingDsl<T>.singleton(body: ProviderDsl.() -> T): ModuleBindingContext =
    declaration(Type.SINGLETON, body)

/**
 * Provides the dependency as an eager singleton. Only once instance (per component) will be created.
 * The instance will be created when the [Component] is created and not lazily the first time it's requested.
 */
@Deprecated(
    message = "Use new DSL syntax under org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("")
)
fun <T> BindingDsl<T>.eagerSingleton(body: ProviderDsl.() -> T): ModuleBindingContext =
    declaration(Type.EAGER_SINGLETON, body)

package org.rewedigital.katana

import org.rewedigital.katana.Declaration.Type
import org.rewedigital.katana.Declaration.Type.*

/**
 * Defines a module (with an optional name).
 *
 * @see Module
 */
fun createModule(name: String? = null, body: Module.() -> Unit) =
    Module(name).apply {
        body.invoke(this)
    }

@DslMarker
annotation class ModuleDslMarker

/**
 * A module consists of dependency declarations and describes how dependencies are provided.
 * Each module should represent a logical unit. For instance there should be separate modules for each functionality
 * of your application.
 *
 * A module can have a name. This is purely optional and just aids in improving dependency injection related exception
 * messages.
 *
 * @see createModule
 * @see Component
 */
@ModuleDslMarker
class Module internal constructor(val name: String? = null) {

    internal val declarations = Katana.environmentContext.mapFactory<Key, Declaration<*>>()()
}

inline fun <reified T> Module.bind(name: String? = null,
                                   body: BindingDsl<T>.() -> Module) =
    body.invoke(BindingDsl(this, T::class.java, name))

/**
 * Provides syntax for declaring how the dependency is provided, either via a [factory] or as a [singleton].
 */
@ModuleDslMarker
class BindingDsl<T>(private val module: Module,
                    private val clazz: Class<T>,
                    private val name: String?) {

    internal fun declaration(type: Type,
                             provider: Provider<T>): Module {
        val key = Key.of(clazz, name)
        val declaration =
            Declaration(key = key,
                        type = type,
                        moduleName = module.name,
                        clazz = clazz,
                        name = name,
                        provider = provider)

        val existingDeclaration = module.declarations[key]
        if (existingDeclaration != null) {
            throw OverrideException(declaration, existingDeclaration)
        }

        module.declarations[key] = declaration
        return module
    }
}

/**
 * Provides the dependency via a factory. A new instance will be created every time the dependency is requested.
 */
fun <T> BindingDsl<T>.factory(body: ProviderDsl.() -> T): Module =
    declaration(FACTORY) { component -> body.invoke(ProviderDsl(component)) }

/**
 * Provides the dependency as a singleton. Only one instance (per component) will be created.
 */
fun <T> BindingDsl<T>.singleton(body: ProviderDsl.() -> T): Module =
    declaration(SINGLETON) { component -> body.invoke(ProviderDsl(component)) }

/**
 * Provides the dependency as an eager singleton. Only once instance (per component) will be created.
 * The instance will be created when the [Component] is created and not lazily the first time it's requested.
 */
fun <T> BindingDsl<T>.eagerSingleton(body: ProviderDsl.() -> T): Module =
    declaration(EAGER_SINGLETON) { component -> body.invoke(ProviderDsl(component)) }

/**
 * Provides syntax for injecting transitive dependencies via [get] within the provider body.
 *
 * @see get
 * @see lazy
 */
class ProviderDsl(val context: ComponentContext)

/**
 * Provides a dependency which has already been declared in the current context (total set of modules of the
 * current component) to be able to inject transitive dependencies within a module.
 */
inline fun <reified T> ProviderDsl.get(name: String? = null) =
    context.injectNow<T>(name)

/**
 * Provides a [Lazy] version of dependency. Should only be required to circumvent a circular dependency cycle.
 * Better solution is to structure classes in a way that circular dependencies are not necessary.
 */
inline fun <reified T> ProviderDsl.lazy(name: String? = null) =
    lazy { get<T>(name) }

package org.rewedigital.katana.dsl

import org.rewedigital.katana.*
import org.rewedigital.katana.dsl.internal.declaration

/**
 * Declares a dependency binding.
 * A new instance will be created every time the dependency is requested.
 *
 * @param name Optional name of binding. See documentation of package [org.rewedigital.katana] for more details.
 * @param internal If `true` binding is only available in current module
 * @param body Body of binding declaration
 *
 * @see ModuleBindingContext.singleton
 * @see ModuleBindingContext.eagerSingleton
 */
inline fun <reified T> ModuleBindingContext.factory(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    declaration(
        context = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Declaration.Type.FACTORY,
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
 * @see ModuleBindingContext.factory
 * @see ModuleBindingContext.eagerSingleton
 */
inline fun <reified T> ModuleBindingContext.singleton(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    declaration(
        context = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Declaration.Type.SINGLETON,
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
 * @see ModuleBindingContext.factory
 * @see ModuleBindingContext.singleton
 */
inline fun <reified T> ModuleBindingContext.eagerSingleton(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    declaration(
        context = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Declaration.Type.EAGER_SINGLETON,
        provider = DefaultProvider(body)
    )

/**
 * Declares an alias of type [A] with optional [name] of an already existing binding of type [T] with optional name
 * [originalName]. An alias is accomplished by and nothing else than a simple [factory] binding.
 *
 * A binding like `alias<MyInterface, MyClass>()` should be conceived as "create alias MyInterface for MyClass".
 */
inline fun <reified A, reified T : A> ModuleBindingContext.alias(
    name: String? = null,
    originalName: String? = null
) {
    factory<A>(name = name) { get<T>(name = originalName) }
}

/**
 * Declares a [Set] based multi-binding.
 *
 * Each [SetBindingContext.factory] or [SetBindingContext.singleton] declaration inside `set { }` contributes
 * to the set.
 *
 * Multiple set bindings of the same **unique** type across different modules and components are conflated into a single
 * [Set] during injection.
 *
 * Example:
 *
 * ```
 * val module1 = Module {
 *   set<String> {
 *     factory { "Hello" }
 *   }
 * }
 *
 * val module2 = Module {
 *   set<String> {
 *      factory { "World" }
 *   }
 * }
 *
 * val component = Component(modules = listOf(module1, module2))
 * val set: Set<String> = component.injectNow() // == setOf("Hello", "World")
 * ```
 *
 * Due to the nature of multi-bindings, singletons declared inside a set are unique per [Module] set declaration
 * **and** [Component]. This means that a set-singleton declared in `module1` and another set-singleton declared in
 * `module2` for the same unique set will result in two singleton instances if both modules are part of the same
 * [Component].
 *
 * @param name Optional name of binding. See documentation of package [org.rewedigital.katana] for more details.
 *
 * @see SetBindingContext.factory
 * @see SetBindingContext.singleton
 */
inline fun <reified T> ModuleBindingContext.set(
    name: Any? = null,
    body: SetBindingContext<T>.() -> Unit
) =
    internalSet(
        name = name,
        body = body
    )

/**
 * internalSet required because of two reified type parameters,
 * where `S` however can be inferred by compiler in call of [set].
 */
@PublishedApi
internal inline fun <reified T, reified S : Set<T>> ModuleBindingContext.internalSet(
    name: Any? = null,
    body: SetBindingContext<T>.() -> Unit
) =
    also {
        SetBindingContext<T>(
            module = module,
            key = Key.of(clazz = T::class.java, name = name)
        ).let { bindingContext ->
            declaration(
                context = this,
                clazz = S::class.java,
                name = name,
                internal = false,
                type = Declaration.Type.SET,
                provider = SetProvider(bindingContext.key)
            )

            body.invoke(bindingContext)
        }
    }

inline fun <reified T> SetBindingContext<T>.factory(
    noinline body: ProviderDsl.() -> T
) =
    declaration(
        context = this,
        clazz = T::class.java,
        name = null,
        internal = false,
        type = Declaration.Type.FACTORY,
        provider = DefaultProvider(body)
    )

inline fun <reified T> SetBindingContext<T>.singleton(
    noinline body: ProviderDsl.() -> T
) =
    declaration(
        context = this,
        clazz = T::class.java,
        name = null,
        internal = false,
        type = Declaration.Type.SINGLETON,
        provider = DefaultProvider(body)
    )

/**
 * Adds an already declared dependency in the context of the [Module] to this [Set].
 *
 * @param T type of the set
 * @param V type of the value added to the set
 */
inline fun <T, reified V : T> SetBindingContext<T>.get(
    name: String? = null
) = declaration(
    context = this,
    clazz = V::class.java,
    name = name,
    internal = false,
    type = Declaration.Type.CUSTOM,
    provider = object : Provider<V> {
        override fun invoke(context: ComponentContext, arg: Any?): V =
            context.injectNow(name = name)
    }
)

/**
 * Declares a custom binding with a custom implementation of [Provider].
 *
 * A custom provider might for instance handle additional arguments passed to [Provider.invoke].
 * This should rarely be used and is rather meant for Katana extensions!
 *
 * @see Component.custom
 */
inline fun <reified T> ModuleBindingContext.custom(
    name: Any? = null,
    internal: Boolean = false,
    provider: Provider<T>
) =
    declaration(
        context = this,
        clazz = T::class.java,
        name = name,
        internal = internal,
        type = Declaration.Type.CUSTOM,
        provider = provider
    )

package org.rewedigital.katana

import org.rewedigital.katana.Declaration.Type.*
import org.rewedigital.katana.Key.ClassKey
import org.rewedigital.katana.Key.NameKey
import org.rewedigital.katana.internal.Logger

/**
 * Along with [Modules][Module] a Component is the heart of dependency injection via Katana.
 *
 * It performs the actual injection and will also hold the singleton instances of dependencies declared as singletons.
 * This is an important aspect of Katana! As long as the same Component reference is used for injection, the same
 * singleton instances are reused. Once the Component is eligible for garbage collection so are the instances hold by
 * this component. The developer is responsible for holding a Component reference and releasing it when necessary. This
 * design was chosen in contrast to other DI libraries that for instance work with a global, singleton state to prevent
 * accidental memory leaks.
 *
 * However great care should be taken when dependent Components are specified via `dependsOn`. If dependent Components
 * are specified and the current Component has a greater scope (lifespan) than the other Components, references to these
 * Components are still held. Dependent Components should always have an equal or greater scope than the current
 * Component.
 *
 * @param modules Modules included by this component
 * @param dependsOn Further components this component depends on
 *
 * @see Module
 * @see inject
 * @see injectNow
 * @see canInject
 * @see Component.plus
 */
class Component(
    modules: Iterable<Module> = emptyList(),
    dependsOn: Iterable<Component> = emptyList()
) {

    constructor(vararg modules: Module) : this(modules = modules.asIterable())

    constructor(vararg dependsOn: Component) : this(dependsOn = dependsOn.asIterable())

    /**
     * Collect all modules including modules declared via `includes`.
     */
    private fun Iterable<Module>.collect(
        acc: MutableMap<Int, Module> = mutableMapOf()
    ): Map<Int, Module> =
        fold(initial = acc) { curAcc, module ->
            if (!curAcc.containsKey(module.id)) {
                curAcc[module.id] = module
                module.includes.collect(curAcc)
            }
            return@fold curAcc
        }

    private val declarations = modules.collect().map { it.value.declarations }.collect {
        Logger.info { "Registering declaration $it" }
    }

    private val instances = Katana.environmentContext.mapFactory().create<Key, Instance<*>>()
    @PublishedApi internal val context = ComponentContext.of(this, dependsOn)

    init {
        val componentDeclarations = dependsOn.map { component -> component.declarations }
        val allDeclarations = (componentDeclarations + declarations).collect()

        if (allDeclarations.isEmpty()) {
            throw ComponentException("No declarations found (did you forget to pass modules and/or parent components?)")
        }

        // Initialize eager singletons
        declarations.values
            .filter { it.type == EAGER_SINGLETON }
            .forEach { declaration ->
                getOrCreateSingleton<Any?>(
                    declaration = declaration,
                    createMessage = { "Created eager singleton instance for ${declaration.key.stringIdentifier}" }
                )
            }
    }

    @Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
    internal fun <T> thisComponentFindInstance(key: Key, internal: Boolean, arg: Any?): Instance<T>? {
        val declaration = declarations[key]
        if (declaration == null || (declaration.internal && !internal)) {
            return null
        } else {
            try {
                Logger.info { "Injecting dependency for ${key.stringIdentifier}" }
                return when (declaration.type) {
                    FACTORY -> declaration.provider(context).let { newInstance ->
                        Logger.debug { "Created instance for ${key.stringIdentifier}" }
                        Instance(newInstance as T)
                    }
                    SINGLETON -> getOrCreateSingleton(
                        declaration = declaration,
                        getMessage = { "Returning existing singleton instance for ${key.stringIdentifier}" },
                        createMessage = { "Created singleton instance for ${key.stringIdentifier}" }
                    )
                    EAGER_SINGLETON -> getOrCreateSingleton(
                        declaration = declaration,
                        getMessage = { "Returning existing eager singleton instance for ${key.stringIdentifier}" },
                        createMessage = { "Created eager singleton instance for ${key.stringIdentifier}" }
                    )
                    SET -> declaration.provider(context).let { newInstance ->
                        Logger.debug { "Created set instance for ${key.stringIdentifier}" }
                        Instance(newInstance as T)
                    }
                    CUSTOM -> declaration.provider(context, arg).let { newInstance ->
                        Logger.debug { "Created custom instance for ${key.stringIdentifier}" }
                        Instance(newInstance as T)
                    }
                }
            } catch (e: KatanaException) {
                throw e
            } catch (e: Exception) {
                throw InstanceCreationException("Could not instantiate $declaration", e)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getOrCreateSingleton(
        declaration: Declaration<*>,
        getMessage: (() -> String)? = null,
        createMessage: (() -> String)? = null
    ) =
        declaration.key.let { key ->
            instances[key]?.let { instance ->
                if (getMessage != null) Logger.debug(getMessage)
                instance as Instance<T>
            } ?: declaration.provider(context).let { newInstance ->
                if (createMessage != null) Logger.debug(createMessage)
                Instance(newInstance as T).also {
                    instances[key] = it
                }
            }
        }

    internal fun thisComponentCanInject(key: Key, internal: Boolean) =
        declarations.filter { if (!internal) !it.value.internal else true }.containsKey(key)

    internal fun canInject(key: Key) =
        context.canInject(key)

    internal fun <T> findInstance(key: Key, arg: Any?) =
        context.findInstance<T>(key = key, arg = arg)

    internal fun findKeysForContext(contextKey: Key): Iterable<Key> =
        declarations.keys.filter { key -> key.contextKey == contextKey }

    /**
     * Returns `true` if this component is capable of injecting requested dependency.
     */
    inline fun <reified T> canInject(name: Any? = null) =
        context.canInject<T>(name)

    /**
     * Injects requested dependency lazily.
     * Should be used like:
     *
     * ```
     * val myDependency: MyDependency by component.inject()
     * ```
     *
     * @see injectNow
     * @see injectOrNull
     * @throws InjectionException
     * @throws InstanceCreationException
     */
    inline fun <reified T> inject(name: Any? = null) =
        context.inject<T>(name)

    /**
     * Injects requested dependency lazily.
     * Injects `null` if no binding could be found.
     *
     * @see inject
     * @see injectNow
     * @throws InstanceCreationException
     */
    inline fun <reified T> injectOrNull(name: Any? = null) =
        context.injectOrNull<T>(name)

    /**
     * Injects requested dependency immediately.
     *
     * @see inject
     * @see injectNowOrNull
     * @throws InjectionException
     * @throws InstanceCreationException
     *
     * TODO: Rename to get()?
     */
    inline fun <reified T> injectNow(name: Any? = null) =
        context.injectNow<T>(name)

    /**
     * Injects requested dependency immediately or returns `null` if no binding could be found.
     *
     * @see inject
     * @see injectNow
     * @throws InstanceCreationException
     *
     * TODO: Rename to getOrNull()?
     */
    inline fun <reified T> injectNowOrNull(name: Any? = null) =
        context.injectNowOrNull<T>(name)

    /**
     * Injects requested dependency, passing custom arguments to [Provider].
     *
     * This should rarely be used and is rather meant for Katana extensions!
     */
    inline fun <reified T> custom(name: Any? = null, arg: Any? = null) =
        context.custom<T>(name, arg = arg)

    /**
     * Alternative syntax for creating a child component that depends on current component
     * plus additional modules.
     */
    operator fun plus(modules: Iterable<Module>) =
        Component(
            modules = modules,
            dependsOn = listOf(this)
        )

    /**
     * Alternative syntax for creating a child component that depends on current component
     * plus additional module.
     */
    operator fun plus(module: Module) = this + listOf(module)

    /**
     * Builder for a [Component].
     *
     * Builder is **not** thread-safe!
     */
    class Builder(
        private val modules: MutableList<Module> = mutableListOf(),
        private val dependsOn: MutableList<Component> = mutableListOf()
    ) {
        /**
         * Sets [Modules][Module] of [Component]. Existing modules will be cleared.
         */
        fun setModules(modules: Iterable<Module>) = apply {
            this.modules.clear()
            this.modules.addAll(modules)
        }

        /**
         * Sets [Modules][Module] of [Component]. Existing modules will be cleared.
         */
        fun setModules(vararg module: Module) = setModules(module.asIterable())

        fun addModule(module: Module) = apply {
            modules.add(module)
        }

        /**
         * Sets depending [Components][Component] of Component. Existing components will be cleared.
         */
        fun setDependsOn(dependsOn: Iterable<Component>) = apply {
            this.dependsOn.clear()
            this.dependsOn.addAll(dependsOn)
        }

        /**
         * Sets depending [Components][Component] of Component. Existing components will be cleared.
         */
        fun setDependsOn(vararg dependsOn: Component) = setDependsOn(dependsOn.asIterable())

        fun addDependsOn(dependsOn: Component) = apply {
            this.dependsOn.add(dependsOn)
        }

        fun build() =
            Component(
                modules = modules,
                dependsOn = dependsOn
            )
    }
}

/**
 * Alternative syntax for creating a child component that depends on current components
 * plus additional modules.
 */
operator fun Iterable<Component>.plus(modules: Iterable<Module>) =
    Component(
        modules = modules,
        dependsOn = this
    )

/**
 * Alternative syntax for creating a child component that depends on current components
 * plus additional module.
 */
operator fun Iterable<Component>.plus(module: Module) =
    this + listOf(module)

/**
 * ComponentContext is used internally to represent the total set of [Components][Component] and thus possible
 * injections for the current context consisting of the current Component and all Components that have been specified
 * via `dependsOn`.
 */
class ComponentContext private constructor(
    private val thisComponent: Component,
    private val dependsOn: Iterable<Component>
) {

    @PublishedApi
    internal fun <T> findInstance(key: Key, internal: Boolean = false, arg: Any? = null): Instance<T>? {
        val instance = thisComponent.thisComponentFindInstance<T>(key, internal, arg)
        return when {
            instance != null -> instance
            else -> {
                val component = dependsOn.find { component -> component.canInject(key) }
                component?.findInstance(key, arg)
            }
        }
    }

    @PublishedApi
    internal fun <T> findInstanceOrThrow(key: Key, internal: Boolean = false, arg: Any? = null): Instance<T> =
        findInstance(key = key, internal = internal, arg = arg)
            ?: throw InjectionException("No binding found for ${key.stringIdentifier}")

    @PublishedApi
    internal fun canInject(key: Key, internal: Boolean = false): Boolean =
        when {
            thisComponent.thisComponentCanInject(key = key, internal = internal) -> true
            else -> dependsOn.any { component -> component.canInject(key) }
        }

    internal fun findKeysForContext(contextKey: Key): Iterable<Key> =
        thisComponent.findKeysForContext(contextKey)
            .plus(dependsOn.flatMap { it.findKeysForContext(contextKey) })

    inline fun <reified T> canInject(name: Any? = null, internal: Boolean = false) =
        canInject(key = Key.of(T::class.java, name), internal = internal)

    inline fun <reified T> inject(name: Any? = null, internal: Boolean = false) = lazy {
        injectNow<T>(name = name, internal = internal)
    }

    inline fun <reified T> injectOrNull(name: Any? = null, internal: Boolean = false) = lazy {
        injectNowOrNull<T>(name = name, internal = internal)
    }

    // TODO: Rename to get()?
    inline fun <reified T> injectNow(name: Any? = null, internal: Boolean = false) =
        findInstanceOrThrow<T>(key = Key.of(T::class.java, name), internal = internal).value

    inline fun <reified T> injectNowOrNull(name: Any? = null, internal: Boolean = false) =
        findInstance<T>(key = Key.of(T::class.java, name), internal = internal)?.value

    inline fun <reified T> custom(name: Any? = null, internal: Boolean = false, arg: Any? = null) =
        findInstanceOrThrow<T>(key = Key.of(T::class.java, name), internal = internal, arg = arg).value

    internal companion object {

        fun of(thisComponent: Component, dependsOn: Iterable<Component>) =
            ComponentContext(thisComponent, dependsOn)
    }
}

private fun Iterable<Declarations>.collect(each: ((Declaration<*>) -> Unit)? = null): Declarations =
    fold(Katana.environmentContext.mapFactory().create()) { acc, currDeclarations ->
        currDeclarations.entries.forEach { entry ->
            val existingDeclaration = acc[entry.key]
            existingDeclaration?.let { declaration ->
                if (declaration.moduleId != entry.value.moduleId && !declaration.internal && !declaration.type.permitsRedeclaration)
                    throw OverrideException(entry.value.toString(), declaration.toString())
            }
            each?.invoke(entry.value)
        }
        acc.apply { putAll(currDeclarations) }
    }

private val Key.stringIdentifier
    get() = when (this) {
        is ClassKey<*> -> "class ${clazz.name}"
        is NameKey -> "name $name"
    }

@PublishedApi
internal class Instance<T>(val value: T)

package org.rewedigital.katana.dsl.compact

import org.rewedigital.katana.ModuleBindingContext
import org.rewedigital.katana.Provider
import org.rewedigital.katana.dsl.ProviderDsl
import org.rewedigital.katana.dsl.custom as newCustom
import org.rewedigital.katana.dsl.eagerSingleton as newEagerSingleton
import org.rewedigital.katana.dsl.factory as newFactory
import org.rewedigital.katana.dsl.singleton as newSingleton

@Deprecated(
    message = "DSL has been moved to package org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("factory(name, internal, body)", "org.rewedigital.katana.dsl.factory")
)
inline fun <reified T> ModuleBindingContext.factory(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    newFactory(name = name, internal = internal, body = body)

@Deprecated(
    message = "DSL has been moved to package org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("singleton(name, internal, body)", "org.rewedigital.katana.dsl.singleton")
)
inline fun <reified T> ModuleBindingContext.singleton(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    newSingleton(name = name, internal = internal, body = body)

@Deprecated(
    message = "DSL has been moved to package org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("eagerSingleton(name, internal, body)", "org.rewedigital.katana.dsl.eagerSingleton")
)
inline fun <reified T> ModuleBindingContext.eagerSingleton(
    name: Any? = null,
    internal: Boolean = false,
    noinline body: ProviderDsl.() -> T
) =
    newEagerSingleton(name = name, internal = internal, body = body)

@Deprecated(
    message = "DSL has been moved to package org.rewedigital.katana.dsl",
    replaceWith = ReplaceWith("custom(name, internal, provider)", "org.rewedigital.katana.dsl.custom")
)
inline fun <reified T> ModuleBindingContext.custom(
    name: Any? = null,
    internal: Boolean = false,
    provider: Provider<T>
) =
    newCustom(name = name, internal = internal, provider = provider)

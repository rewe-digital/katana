package org.rewedigital.katana.dsl.internal

import org.rewedigital.katana.*

@PublishedApi
internal fun <CTX : BindingContext, T> declaration(
    context: CTX,
    clazz: Class<T>,
    name: Any?,
    internal: Boolean,
    type: Declaration.Type,
    provider: Provider<T>
): CTX {
    val key = Key.of(
        clazz = clazz,
        name = name,
        contextKey = context.key,
        increment = context.increment
    )

    val declaration =
        Declaration(
            key = key,
            type = type,
            moduleId = context.module.id,
            moduleName = context.module.name,
            clazz = clazz,
            name = name,
            provider = provider,
            internal = internal
        )

    val existingDeclaration = context.module.declarations[key]
    if (existingDeclaration != null) {
        throw OverrideException(declaration.toString(), existingDeclaration.toString())
    }

    context.module.declarations[key] = declaration
    return context
}

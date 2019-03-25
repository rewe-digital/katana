package org.rewedigital.katana.dsl.internal

import org.rewedigital.katana.*

@PublishedApi
internal fun <T> moduleDeclaration(
    module: Module,
    clazz: Class<T>,
    name: Any?,
    internal: Boolean,
    type: Declaration.Type,
    provider: Provider<T>
): Module {

    val key = Key.of(clazz, name)
    val declaration =
        Declaration(key = key,
                    type = type,
                    moduleName = module.name,
                    clazz = clazz,
                    name = name,
                    provider = provider,
                    internal = internal)

    val existingDeclaration = module.declarations[key]
    if (existingDeclaration != null) {
        throw OverrideException(declaration.toString(), existingDeclaration.toString())
    }

    module.declarations[key] = declaration
    return module
}

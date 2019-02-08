package org.rewedigital.katana

import org.rewedigital.katana.dsl.ProviderDsl

/**
 * Lambda which provides an instance of T.
 *
 * The [ComponentContext] of the current context is passed to the lambda which enables transitive dependency injection, e.g.
 * injection of dependencies within a module.
 *
 * @see Module
 * @see ProviderDsl
 */
internal typealias Provider<T> = (ComponentContext) -> T

internal typealias Declarations = Map<Key, Declaration<*>>

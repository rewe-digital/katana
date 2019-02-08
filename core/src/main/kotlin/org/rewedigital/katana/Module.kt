package org.rewedigital.katana

import org.rewedigital.katana.dsl.ModuleDslMarker

/**
 * Defines a module (with an optional name).
 *
 * @see Module
 */
fun createModule(name: String? = null, body: Module.() -> Unit) =
    Module(name).apply {
        body.invoke(this)
    }

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

    internal val declarations = Katana.environmentContext.mapFactory().create<Key, Declaration<*>>()
}

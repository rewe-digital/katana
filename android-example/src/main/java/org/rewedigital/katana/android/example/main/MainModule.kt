package org.rewedigital.katana.android.example.main

import android.content.Context
import org.rewedigital.katana.createModule

/**
 * This module demonstrates how the current [Context] can be injected safely.
 * In order for this to work without introducing leaks the module must be installed in a
 * [org.rewedigital.katana.Component] which is *only* referenced from the same Activity that
 * created the component.
 *
 * @see MainActivity
 */
fun createMainModule(context: Context) = createModule {

    bind<Context> { singleton { context } }

    bind<Greeter> { factory { Greeter(get()) } }
}

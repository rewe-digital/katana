package org.rewedigital.katana.android.example.inject

import android.content.Context
import org.rewedigital.katana.android.example.KatanaApp
import org.rewedigital.katana.createModule

const val APP_CONTEXT = "APP_CONTEXT"

/**
 * The application module provides the application [Context] via the named binding "APP_CONTEXT".
 */
fun createApplicationModule(app: KatanaApp) = createModule {

    bind<Context>(APP_CONTEXT) { singleton { app } }
}

package org.rewedigital.katana.android.example.inject

import android.content.Context
import android.content.res.Resources
import org.rewedigital.katana.createModule

/**
 * This module may provide Android specific classes like [Resources],
 * [android.content.SharedPreferences], system services etc.
 *
 * @see createApplicationModule
 */
val androidModule = createModule {

    bind<Resources> { factory { get<Context>(APP_CONTEXT).resources } }
}

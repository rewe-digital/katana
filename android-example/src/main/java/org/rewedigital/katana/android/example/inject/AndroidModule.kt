package org.rewedigital.katana.android.example.inject

import android.content.Context
import android.content.res.Resources
import org.rewedigital.katana.android.modules.APPLICATION_CONTEXT
import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory
import org.rewedigital.katana.get

/**
 * This module may provide Android specific classes like [Resources],
 * [android.content.SharedPreferences], system services etc.
 *
 * @see org.rewedigital.katana.android.modules.createApplicationModule
 */
val androidModule = createModule {

    bind<Resources> { factory { get<Context>(APPLICATION_CONTEXT).resources } }
}

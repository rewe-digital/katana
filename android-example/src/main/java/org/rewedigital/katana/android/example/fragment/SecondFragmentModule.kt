package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory

val secondFragmentModule = createModule {

    bind<String>(name = "FRAGMENT_DEPENDENCY2") { factory { "FRAGMENT_DEPENDENCY2" } }
}

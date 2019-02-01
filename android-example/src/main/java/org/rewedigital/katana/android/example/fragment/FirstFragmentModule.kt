package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory

val firstFragmentModule = createModule {

    bind<String>(name = "FRAGMENT_DEPENDENCY1") { factory { "FRAGMENT_DEPENDENCY1" } }
}

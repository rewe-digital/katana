package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory

val fragmentActivityModule = createModule {

    bind<String>(name = "SOME_DEPENDENCY") { factory { "SOME_DEPENDENCY" } }
}

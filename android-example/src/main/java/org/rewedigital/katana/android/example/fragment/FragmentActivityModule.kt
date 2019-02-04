package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory

const val SOME_DEPENDENCY = "SOME_DEPENDENCY"

val fragmentActivityModule = createModule {

    bind<String>(SOME_DEPENDENCY) { factory { "SOME_DEPENDENCY" } }
}

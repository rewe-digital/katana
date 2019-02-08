package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.createModule
import org.rewedigital.katana.dsl.compact.factory

const val SOME_DEPENDENCY = "SOME_DEPENDENCY"

val fragmentActivityModule = createModule {

    factory(name = SOME_DEPENDENCY) { "SOME_DEPENDENCY" }
}

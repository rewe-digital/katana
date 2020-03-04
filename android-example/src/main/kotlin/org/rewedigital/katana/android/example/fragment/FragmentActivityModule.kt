package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.factory

const val SOME_DEPENDENCY = "SOME_DEPENDENCY"

val FragmentActivityModule = Module {

    factory(name = SOME_DEPENDENCY) { "SOME_DEPENDENCY" }
}

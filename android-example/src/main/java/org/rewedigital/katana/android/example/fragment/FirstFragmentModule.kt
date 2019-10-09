package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.factory

const val FRAGMENT_DEPENDENCY1 = "FRAGMENT_DEPENDENCY1"

val firstFragmentModule = Module {

    factory(name = FRAGMENT_DEPENDENCY1) { "FRAGMENT_DEPENDENCY1" }
}

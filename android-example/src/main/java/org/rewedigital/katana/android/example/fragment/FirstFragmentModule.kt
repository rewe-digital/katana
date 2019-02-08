package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.createModule
import org.rewedigital.katana.dsl.compact.factory

const val FRAGMENT_DEPENDENCY1 = "FRAGMENT_DEPENDENCY1"

val firstFragmentModule = createModule {

    factory(name = FRAGMENT_DEPENDENCY1) { "FRAGMENT_DEPENDENCY1" }
}

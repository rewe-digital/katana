package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.android.example.fragment.inject.Container
import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory
import org.rewedigital.katana.get

const val FRAGMENT_DEPENDENCY2 = "FRAGMENT_DEPENDENCY2"

val secondFragmentModule = createModule {

    bind<String>(name = "FRAGMENT_DEPENDENCY2") { factory { "FRAGMENT_DEPENDENCY2" } }

    bind<Container> { factory { Container(get(SOME_DEPENDENCY), get(FRAGMENT_DEPENDENCY2)) } }
}

package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.Module
import org.rewedigital.katana.android.example.fragment.view.FirstFragment
import org.rewedigital.katana.android.example.fragment.view.SecondFragment
import org.rewedigital.katana.androidx.fragment.KatanaFragmentFactory
import org.rewedigital.katana.dsl.component
import org.rewedigital.katana.dsl.factory
import org.rewedigital.katana.dsl.singleton

val FragmentFactoryModule = Module(
    name = "FragmentFactoryModule"
) {

    singleton {
        KatanaFragmentFactory(component)
            .handlesFragment<FirstFragment>()
            .handlesFragment<SecondFragment>()
    }

    factory { FirstFragment(component) }

    factory { SecondFragment(component) }
}

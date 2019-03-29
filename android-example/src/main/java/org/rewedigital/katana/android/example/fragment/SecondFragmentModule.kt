package org.rewedigital.katana.android.example.fragment

import org.rewedigital.katana.Module
import org.rewedigital.katana.android.example.fragment.inject.Container
import org.rewedigital.katana.android.example.fragment.model.SecondFragmentViewModel
import org.rewedigital.katana.androidx.viewmodel.viewModel
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.get

const val FRAGMENT_DEPENDENCY2 = "FRAGMENT_DEPENDENCY2"

val secondFragmentModule = Module {

    factory(name = FRAGMENT_DEPENDENCY2) { "FRAGMENT_DEPENDENCY2" }

    factory { Container(get(SOME_DEPENDENCY), get(FRAGMENT_DEPENDENCY2)) }

    viewModel { SecondFragmentViewModel() }
}

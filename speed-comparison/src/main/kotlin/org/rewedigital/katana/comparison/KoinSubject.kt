package org.rewedigital.katana.comparison

import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.dsl.bind
import org.koin.dsl.module

class KoinSubject : Subject, KoinComponent {

    override fun setup() {
        val module = module {

            single { MySingleton() }

            factory { MyDependencyImpl(get()) } bind MyDependency::class

            factory { MyDependency2(get()) }
        }

        startKoin {
            modules(module)
        }
    }

    override fun execute() {
        val dependency: MyDependency by inject()
        val dependency2: MyDependency2 by inject()

        dependency.execute()
        dependency2.execute()
    }

    override fun shutdown() {
        stopKoin()
    }
}

package org.rewedigital.katana.comparison

import org.koin.dsl.module.module
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject

class KoinSubject : Subject, KoinComponent {

    override fun setup() {
        val module = module {

            single { MySingleton() }

            factory { MyDependencyImpl(get()) } bind MyDependency::class

            factory { MyDependency2(get()) }
        }

        startKoin(list = listOf(module),
                  logger = EmptyLogger())
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

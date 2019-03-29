package org.rewedigital.katana.comparison

import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import org.rewedigital.katana.inject

class KatanaSubject : Subject, KatanaTrait {

    override lateinit var component: Component

    override fun setup() {
        val module = Module {

            singleton { MySingleton() }

            factory<MyDependency> { MyDependencyImpl(get()) }

            factory { MyDependency2(get()) }
        }

        component = Component(module)
    }

    override fun execute() {
        val dependency: MyDependency by inject()
        val dependency2: MyDependency2 by inject()

        dependency.execute()
        dependency2.execute()
    }

    override fun shutdown() {
    }
}

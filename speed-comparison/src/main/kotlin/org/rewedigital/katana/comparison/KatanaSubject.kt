package org.rewedigital.katana.comparison

import org.rewedigital.katana.*

class KatanaSubject : Subject, KatanaTrait {

    override lateinit var component: Component

    override fun setup() {
        val module = createModule {

            bind<MySingleton> { singleton { MySingleton() } }

            bind<MyDependency> { factory { MyDependencyImpl(get()) } }

            bind<MyDependency2> { factory { MyDependency2(get()) } }
        }

        component = createComponent(module)
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

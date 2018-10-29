package org.rewedigital.katana.comparison

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

class KodeinSubject : Subject {

    private lateinit var kodein: Kodein

    override fun setup() {
        kodein = Kodein {

            bind<MySingleton>() with singleton { MySingleton() }

            bind<MyDependency>() with provider {
                MyDependencyImpl(instance())
            }

            bind<MyDependency2>() with provider {
                MyDependency2(instance())
            }
        }
    }

    override fun execute() {
        val dependency: MyDependency by kodein.instance()
        val dependency2: MyDependency2 by kodein.instance()

        dependency.execute()
        dependency2.execute()
    }

    override fun shutdown() {
    }
}

package org.rewedigital.katana

import org.amshove.kluent.shouldThrow
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object KatanaTraitTests : Spek(
    {

        @Suppress("unused")
        class DelegateBeforeInitialization : KatanaTrait {

            val myComponent: MyComponent by inject()

            val module = createModule {
                bind<MyComponent> { singleton { MyComponentA() } }
            }

            override val component: Component = createComponent(module)
        }

        describe("KatanaTrait") {

            it("should throw meaningful exception when component was not initialized") {
                {
                    DelegateBeforeInitialization()
                }.shouldThrow(ComponentNotInitializedException::class)
            }
        }
    })

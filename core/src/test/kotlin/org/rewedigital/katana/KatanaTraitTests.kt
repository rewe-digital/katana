package org.rewedigital.katana

import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class KatanaTraitTests : Spek(
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

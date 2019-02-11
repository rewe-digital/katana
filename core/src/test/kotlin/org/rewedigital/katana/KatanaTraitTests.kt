package org.rewedigital.katana

import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.compact.singleton
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object KatanaTraitTests : Spek(
    {
        val module = createModule {
            singleton<MyComponent> { MyComponentA() }
        }

        class TestKatanaTrait : KatanaTrait {

            override val component = createComponent(module)

            val myComponent by inject<MyComponent>()

            val myComponent2 = injectNow<MyComponent>()
        }

        val trait by memoized { TestKatanaTrait() }

        @Suppress("unused")
        class DelegateBeforeInitialization : KatanaTrait {

            val myComponent by inject<MyComponent>()

            override val component = createComponent(module)
        }

        describe("KatanaTrait") {

            it("should throw meaningful exception when component was not initialized") {
                {
                    DelegateBeforeInitialization()
                }.shouldThrow(ComponentNotInitializedException::class)
            }

            context("injectNow") {

                it("should inject dependency") {
                    trait.myComponent shouldBeInstanceOf MyComponentA::class.java
                }
            }

            context("inject") {

                it("should inject dependency") {
                    trait.myComponent2 shouldBeInstanceOf MyComponentA::class.java
                }
            }

            context("canInject") {

                it("should work") {
                    trait.canInject<MyComponent>() shouldEqual true
                    trait.canInject<String>() shouldEqual false
                }
            }
        }
    })

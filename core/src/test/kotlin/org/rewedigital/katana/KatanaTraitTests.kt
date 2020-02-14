package org.rewedigital.katana

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.singleton
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object KatanaTraitTests : Spek(
    {
        val module = Module {
            singleton<MyComponent> { MyComponentA() }
        }

        class TestKatanaTrait : KatanaTrait {

            override val component = Component(module)

            val myComponent by inject<MyComponent>()

            val myComponent2 = injectNow<MyComponent>()
        }

        val trait by memoized { TestKatanaTrait() }

        @Suppress("unused")
        class DelegateBeforeInitialization : KatanaTrait {

            val myComponent by inject<MyComponent>()

            override val component = Component(module)
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
                    trait.canInject<MyComponent>() shouldBeEqualTo true
                    trait.canInject<String>() shouldBeEqualTo false
                }
            }
        }
    })

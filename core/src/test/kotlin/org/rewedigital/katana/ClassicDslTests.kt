package org.rewedigital.katana

import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.rewedigital.katana.dsl.classic.bind
import org.rewedigital.katana.dsl.classic.eagerSingleton
import org.rewedigital.katana.dsl.classic.factory
import org.rewedigital.katana.dsl.classic.singleton
import org.rewedigital.katana.dsl.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ClassicDslTests : Spek(
    {
        var invocations = 0
        var eagerInvocations = 0

        val module by memoized {
            createModule {

                bind<MyComponent> { factory { MyComponentA() } }

                bind<MyComponentB<MyComponent>> { factory { MyComponentB(get()) } }

                bind<String>(name = "A") { factory { "Hello" } }

                bind<String>(name = "B") { singleton { invocations++; "World" } }

                bind<String>(name = "C") { eagerSingleton { eagerInvocations++; "World 2" } }
            }
        }

        val component by memoized { createComponent(module) }

        beforeEachTest {
            invocations = 0
            eagerInvocations = 0
        }

        describe("Classic DSL") {

            context("canInject") {

                it("should workd") {
                    component.canInject<MyComponent>() shouldEqual true
                    component.canInject<MyComponentB<MyComponent>>() shouldEqual true
                    component.canInject<String>(name = "A") shouldEqual true
                    component.canInject<String>(name = "B") shouldEqual true
                    component.canInject<String>(name = "C") shouldEqual true

                    component.canInject<MyComponentC<*, *>>() shouldEqual false
                }
            }

            context("inject") {

                it("should work") {
                    val myComponent by component.inject<MyComponent>()
                    myComponent shouldBeInstanceOf MyComponentA::class

                    val myComponentB by component.inject<MyComponentB<MyComponent>>()
                    myComponentB shouldBeInstanceOf MyComponentB::class

                    val a by component.inject<String>(name = "A")
                    a shouldEqual "Hello"

                    val b by component.inject<String>(name = "B")
                    val b2 by component.inject<String>(name = "B")
                    b shouldEqual "World"
                    b2 shouldEqual "World"
                    invocations shouldEqual 1

                    val c by component.inject<String>(name = "C")
                    val c2 by component.inject<String>(name = "C")
                    c shouldEqual "World 2"
                    c2 shouldEqual "World 2"
                    eagerInvocations shouldEqual 1
                }
            }

            context("injectNow") {

                it("should work") {
                    component.injectNow<MyComponent>() shouldBeInstanceOf MyComponentA::class

                    component.injectNow<MyComponentB<MyComponent>>() shouldBeInstanceOf MyComponentB::class

                    component.injectNow<String>(name = "A") shouldEqual "Hello"

                    component.injectNow<String>(name = "B") shouldEqual "World"

                    component.injectNow<String>(name = "C") shouldEqual "World 2"
                }
            }
        }
    })

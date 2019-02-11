package org.rewedigital.katana

import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ComponentDependsOnTests : Spek(
    {
        describe("Injection with multiple components") {

            it("should inject dependencies") {
                val module1 = createModule {

                    factory { "Hello world" }

                    factory("another") { "Hello world 2" }
                }

                val component1 = createComponent(module1)

                val module2 = createModule {

                    factory { 1337 }
                }

                val component2 = createComponent(module2)

                val module3 = createModule {

                    factory { MyComponentC<String, Int>(get(), get()) }
                }

                val component3 = createComponent(modules = listOf(module3),
                                                 dependsOn = listOf(component1, component2))

                component3.canInject<MyComponentC<String, Int>>() shouldEqual true
                component3.canInject<Int>() shouldEqual true
                component3.canInject<String>() shouldEqual true
                component3.canInject<String>("another") shouldEqual true
                component3.canInject<MyComponent>() shouldEqual false

                val myComponent: MyComponentC<String, Int> by component3.inject()

                myComponent.value shouldEqual "Hello world"
                myComponent.value2 shouldEqual 1337
            }

            it("should inject dependencies over multiple component tiers") {

                val module1 = createModule {

                    factory { "Hello world" }
                }

                val component1 = createComponent(module1)

                val component2 = createComponent(component1)

                val module3 = createModule {

                    factory { 1337 }
                }

                val component3 = createComponent(
                    modules = listOf(module3),
                    dependsOn = listOf(component2))

                component3.canInject<String>() shouldEqual true
                component3.canInject<Int>() shouldEqual true
                val string: String by component3.inject()
                val int: Int by component3.inject()

                string shouldEqual "Hello world"
                int shouldEqual 1337
            }

            it("should throw override exception for overrides in components") {

                val module1 = createModule {

                    factory<MyComponent> { MyComponentA() }
                }

                val component1 = createComponent(module1)

                val module2 = createModule("module1") {

                    factory<MyComponent> { MyComponentA() }
                }

                val component2 = createComponent(module2)

                val module3 = createModule {

                    factory { "Hello world" }
                }

                val fn = {
                    createComponent(modules = listOf(module3),
                                    dependsOn = listOf(component1, component2))
                }

                fn shouldThrow OverrideException::class
            }

            it("should inject null values over multiple component tiers") {

                val module = createModule {

                    factory<MyComponent?> { null }
                }

                val component = createComponent(module)
                val component2 = createComponent(component)

                val injection: MyComponent? = component2.injectNow()

                injection shouldEqual null
            }

            it("should not inject internal bindings over multiple component tiers") {
                val module = createModule {

                    factory(name = "internal", internal = true) { "Hello world" }

                    factory<MyComponent> { MyComponentB<String>(get("internal")) }
                }

                val component = createComponent(module)
                val component2 = createComponent(component)

                component2.canInject<String>("internal") shouldEqual false
                component2.canInject<MyComponent>() shouldEqual true

                component2.injectNow<MyComponent>() shouldBeInstanceOf MyComponentB::class

                val fn = {
                    component2.injectNow<String>("internal")
                }

                fn shouldThrow InjectionException::class
            }

            it("plus operator should work as expected") {

                val module1 = createModule {

                    factory { "Hello world" }
                }

                val module2 = createModule {

                    factory { 1234 }
                }

                val module3 = createModule {

                    factory("NAME") { 4321 }
                }

                val module4 = createModule {

                    factory("NAME2") { 1337 }
                }

                val component1 = createComponent(module1)
                val component2 = component1 + listOf(module2)
                val component3 = component1 + module2

                component2.injectNow<String>() shouldEqual "Hello world"
                component2.injectNow<Int>() shouldEqual 1234

                component3.injectNow<String>() shouldEqual "Hello world"
                component3.injectNow<Int>() shouldEqual 1234

                val component4 = createComponent(module3)
                val component5 = listOf(component2, component4) + listOf(module4)
                val component6 = listOf(component2, component4) + module4

                component5.injectNow<String>() shouldEqual "Hello world"
                component5.injectNow<Int>() shouldEqual 1234
                component5.injectNow<Int>("NAME") shouldEqual 4321
                component5.injectNow<Int>("NAME2") shouldEqual 1337

                component6.injectNow<String>() shouldEqual "Hello world"
                component6.injectNow<Int>() shouldEqual 1234
                component6.injectNow<Int>("NAME") shouldEqual 4321
                component6.injectNow<Int>("NAME2") shouldEqual 1337
            }

            // TODO: Fix this
            xit("should allow \"empty\" component when it only has transitive dependencies") {

                val module = createModule {

                    factory { "Hello world" }
                }

                val component = createComponent(module)
                val component2 = createComponent(component)
                val component3 = createComponent(component2)

                val injection: String = component3.injectNow()

                injection shouldEqual "Hello world"
            }
        }
    })

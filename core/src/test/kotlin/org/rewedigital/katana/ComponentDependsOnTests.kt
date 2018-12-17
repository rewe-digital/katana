package org.rewedigital.katana

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xit
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ComponentDependsOnTests : Spek(
    {
        describe("Injection with multiple components") {

            it("should inject dependencies") {
                val module1 = createModule {

                    bind<String> { factory { "Hello world" } }

                    bind<String>("another") { factory { "Hello world 2" } }
                }

                val component1 = createComponent(module1)

                val module2 = createModule {

                    bind<Int> { factory { 1337 } }
                }

                val component2 = createComponent(module2)

                val module3 = createModule {

                    bind<MyComponentC<String, Int>> {
                        factory {
                            MyComponentC(get(),
                                         get())
                        }
                    }
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

                    bind<String> { factory { "Hello world" } }
                }

                val component1 = createComponent(module1)

                val component2 = createComponent(component1)

                val module3 = createModule {

                    bind<Int> { factory { 1337 } }
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

                    bind<MyComponent> { factory { MyComponentA() } }
                }

                val component1 = createComponent(module1)

                val module2 = createModule("module1") {

                    bind<MyComponent> { factory { MyComponentA() } }
                }

                val component2 = createComponent(module2)

                val module3 = createModule {

                    bind<String> { factory { "Hello world" } }
                }

                val fn = {
                    createComponent(modules = listOf(module3),
                                    dependsOn = listOf(component1, component2))
                }

                fn shouldThrow OverrideException::class
            }

            it("should inject null values over multiple component tiers") {

                val module = createModule {

                    bind<MyComponent?> { factory { null } }
                }

                val component = createComponent(module)
                val component2 = createComponent(component)

                val injection: MyComponent? = component2.injectNow()

                injection shouldEqual null
            }

            // TODO: Fix this
            xit("should allow \"empty\" component when it only has transitive dependencies") {

                val module = createModule {

                    bind<String> { factory { "Hello world" } }
                }

                val component = createComponent(module)
                val component2 = createComponent(component)
                val component3 = createComponent(component2)

                val injection: String = component3.injectNow()

                injection shouldEqual "Hello world"
            }
        }
    })

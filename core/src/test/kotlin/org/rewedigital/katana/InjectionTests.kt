package org.rewedigital.katana

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object InjectionTests : Spek(
    {
        val module1 = createModule {

            bind<String> { factory { "Hello world" } }
        }

        val component1 = createComponent(module1)

        val module2 = createModule("module1") {

            bind<MyComponent> { singleton { MyComponentA() } }
        }

        val module3 = createModule("module2") {

            bind<MyComponent>("myComponent2") {
                factory {
                    MyComponentB<String>(component1.injectNow())
                }
            }

            bind<MyComponentB<String>> { factory { get("myComponent2") } }
        }

        val component2 = createComponent(module2, module3)

        describe("Injection") {

            it("should inject non-conflicting dependencies") {
                val string: String by component1.inject()
                val myComponent: MyComponent by component2.inject()
                val myComponent2: MyComponent by component2.inject(name = "myComponent2")
                val myComponent3: MyComponentB<String> by component2.inject()

                string shouldEqual "Hello world"
                myComponent shouldBeInstanceOf MyComponentA::class
                myComponent2 shouldBeInstanceOf MyComponentB::class
                myComponent3.value shouldEqual "Hello world"
            }

            it("should create singletons just once") {
                val myComponent: MyComponent by component2.inject()
                val myComponent2: MyComponent by component2.inject()

                myComponent shouldBeInstanceOf MyComponentA::class
                myComponent2 shouldBe myComponent
            }

            it("should provide injection possibility via canInject()") {
                component1.canInject<String>() shouldEqual true
                component2.canInject<MyComponent>() shouldEqual true
                component2.canInject<MyComponent>("myComponent2") shouldEqual true

                component2.canInject<String>() shouldEqual false
                component2.canInject<MyComponent>("myComponent3") shouldEqual false
            }

            it("should provide dependencies across createModule boundaries") {
                val module4 = createModule {

                    bind<MyComponentA> { factory { MyComponentA() } }
                }

                val module5 = createModule {

                    bind<MyComponentB<MyComponentA>> {
                        factory {
                            MyComponentB(get())
                        }
                    }
                }

                val component3 = createComponent(module5, module4)
                val myComponent: MyComponentB<MyComponentA> by component3.inject()

                myComponent.value shouldBeInstanceOf MyComponentA::class
            }

            it("should throw exception when dependency was not declared") {
                val module = createModule {

                    bind<Int> { factory { 1337 } }
                }

                val component3 = createComponent(module)

                val fn = {
                    val myComponent: MyComponent by component3.inject()
                    myComponent shouldBeInstanceOf MyComponent::class
                }

                fn shouldThrow InjectionException::class
            }

            it("eager singletons should be initialized when component is created") {
                var timesSingletonCreated = 0
                val module = createModule {

                    bind<String> { factory { "Hello world" } }

                    bind<MyComponentB<String>> {
                        eagerSingleton {
                            timesSingletonCreated++; MyComponentB(
                            get())
                        }
                    }
                }

                val component = createComponent(module)

                timesSingletonCreated shouldEqual 1

                val myComponent: MyComponentB<String> by component.inject()
                myComponent.value shouldEqual "Hello world"

                timesSingletonCreated shouldEqual 1
            }

            it("circular dependencies should fail") {
                val module = createModule {

                    bind<A> { singleton { A(get()) } }

                    bind<B> { singleton { B(get()) } }
                }

                val component = createComponent(module)

                val fn = {
                    component.injectNow<A>()
                }

                fn shouldThrow StackOverflowError::class
            }

            it("circular dependencies with lazy() should work") {
                val module = createModule {

                    bind<A2> { singleton { A2(lazy()) } }

                    bind<B2> { singleton { B2(get()) } }
                }

                val component = createComponent(module)

                component.injectNow<A2>()
                component.injectNow<B2>()
            }

            it("permit injection of null values") {
                val module = createModule {

                    bind<A?> { factory { null } }
                }

                val component = createComponent(module)

                val injection: A? = component.injectNow()

                injection shouldEqual null
            }

            it("permit internal module bindings") {
                val module = createModule {

                    bind<String>(name = "internal", internal = true) { factory { "Hello world" } }

                    bind<MyComponent> { factory { MyComponentB<String>(get("internal")) } }
                }

                val component = createComponent(module)

                component.canInject<String>("internal") shouldEqual false
                component.canInject<MyComponent>() shouldEqual true

                val injection = component.injectNow<MyComponent>()

                injection shouldBeInstanceOf MyComponentB::class
                (injection as MyComponentB<*>).value shouldEqual "Hello world"

                val fn = {
                    component.injectNow<String>("internal")
                }

                fn shouldThrow InjectionException::class
            }

            it("singleton injection should allow null values") {
                var invocations = 0
                val module = createModule {

                    bind<String?>("singleton") { singleton { invocations++; null } }
                }

                val component = createComponent(module)

                component.canInject<String?>("singleton") shouldEqual true
                component.injectNow<String?>("singleton") shouldEqual null
                component.injectNow<String?>("singleton") shouldEqual null

                invocations shouldEqual 1
            }

            it("eagerSingleton injection should allow null values") {
                var invocations = 0
                val module = createModule {

                    bind<String?>("eagerSingleton") { eagerSingleton { invocations++; null } }
                }

                val component = createComponent(module)

                component.canInject<String?>("eagerSingleton") shouldEqual true
                component.injectNow<String?>("eagerSingleton") shouldEqual null
                component.injectNow<String?>("eagerSingleton") shouldEqual null

                invocations shouldEqual 1
            }
        }
    })

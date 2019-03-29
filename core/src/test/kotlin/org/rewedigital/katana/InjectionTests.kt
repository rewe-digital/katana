package org.rewedigital.katana

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.compact.eagerSingleton
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import org.rewedigital.katana.dsl.lazy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object InjectionTests : Spek(
    {
        beforeGroup {
            Katana.environmentContext = TestEnvironmentContext
        }

        val module1 = Module {

            factory { "Hello world" }
        }

        val component1 = Component(module1)

        val module2 = Module("module1") {

            singleton<MyComponent> { MyComponentA() }
        }

        val module3 = Module("module2") {

            factory<MyComponent>("myComponent2") {
                MyComponentB<String>(component1.injectNow())
            }

            factory<MyComponentB<String>> { get("myComponent2") }
        }

        val component2 = Component(module2, module3)

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

            it("should provide dependencies across module boundaries") {
                val module4 = Module {

                    factory { MyComponentA() }
                }

                val module5 = Module {

                    factory {
                        MyComponentB<MyComponentA>(get())
                    }
                }

                val component3 = Component(module5, module4)
                val myComponent: MyComponentB<MyComponentA> by component3.inject()

                myComponent.value shouldBeInstanceOf MyComponentA::class
            }

            it("should throw exception when dependency was not declared") {
                val module = Module {

                    factory { 1337 }
                }

                val component3 = Component(module)

                val fn = {
                    val myComponent: MyComponent by component3.inject()
                    myComponent shouldBeInstanceOf MyComponent::class
                }

                fn shouldThrow InjectionException::class
            }

            it("eager singletons should be initialized when component is created") {
                var timesSingletonCreated = 0
                val module = Module {

                    factory { "Hello world" }

                    eagerSingleton<MyComponentB<String>> {
                        timesSingletonCreated++; MyComponentB(
                        get()
                    )
                    }
                }

                val component = Component(module)

                timesSingletonCreated shouldEqual 1

                val myComponent: MyComponentB<String> by component.inject()
                myComponent.value shouldEqual "Hello world"

                timesSingletonCreated shouldEqual 1
            }

            it("transitive eager singletons should be initialized properly") {
                var timesFirstSingletonCreated = 0
                var timesSecondSingletonCreated = 0

                val module = Module {

                    eagerSingleton(name = "eagerSingleton1") {
                        timesFirstSingletonCreated++
                        "eagerSingleton1 ${get<String>("eagerSingleton2")}"
                    }

                    eagerSingleton(name = "eagerSingleton2") {
                        timesSecondSingletonCreated++
                        "eagerSingleton2"
                    }
                }

                val component = Component(module)

                timesFirstSingletonCreated shouldEqual 1
                timesSecondSingletonCreated shouldEqual 1

                component.injectNow<String>("eagerSingleton1") shouldEqual "eagerSingleton1 eagerSingleton2"
                component.injectNow<String>("eagerSingleton2") shouldEqual "eagerSingleton2"
            }

            it("circular dependencies should fail") {
                val module = Module {

                    singleton { A(get()) }

                    singleton { B(get()) }
                }

                val component = Component(module)

                val fn = {
                    component.injectNow<A>()
                }

                fn shouldThrow StackOverflowError::class
            }

            it("circular dependencies with lazy() should work") {
                val module = Module {

                    singleton { A2(lazy()) }

                    singleton { B2(get()) }
                }

                val component = Component(module)

                component.injectNow<A2>()
                component.injectNow<B2>()
            }

            it("permit injection of null values") {
                val module = Module {

                    factory<A?> { null }
                }

                val component = Component(module)

                val injection: A? = component.injectNow()

                injection shouldEqual null
            }

            it("permit internal module bindings") {
                val module = Module {

                    factory(name = "internal", internal = true) { "Hello world" }

                    factory<MyComponent> { MyComponentB<String>(get("internal")) }
                }

                val component = Component(module)

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
                val module = Module {

                    singleton<String?>("singleton") { invocations++; null }
                }

                val component = Component(module)

                component.canInject<String?>("singleton") shouldEqual true
                component.injectNow<String?>("singleton") shouldEqual null
                component.injectNow<String?>("singleton") shouldEqual null

                invocations shouldEqual 1
            }

            it("eagerSingleton injection should allow null values") {
                var invocations = 0
                val module = Module {

                    eagerSingleton<String?>("eagerSingleton") { invocations++; null }
                }

                val component = Component(module)

                component.canInject<String?>("eagerSingleton") shouldEqual true
                component.injectNow<String?>("eagerSingleton") shouldEqual null
                component.injectNow<String?>("eagerSingleton") shouldEqual null

                invocations shouldEqual 1
            }
        }
    })

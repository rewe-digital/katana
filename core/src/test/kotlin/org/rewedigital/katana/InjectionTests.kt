package org.rewedigital.katana

import org.amshove.kluent.*
import org.rewedigital.katana.dsl.*
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

                string shouldBeEqualTo "Hello world"
                myComponent shouldBeInstanceOf MyComponentA::class
                myComponent2 shouldBeInstanceOf MyComponentB::class
                myComponent3.value shouldBeEqualTo "Hello world"
            }

            it("should create singletons just once") {
                val myComponent: MyComponent by component2.inject()
                val myComponent2: MyComponent by component2.inject()

                myComponent shouldBeInstanceOf MyComponentA::class
                myComponent2 shouldBe myComponent
            }

            it("should provide injection possibility via canInject()") {
                component1.canInject<String>() shouldBeEqualTo true
                component2.canInject<MyComponent>() shouldBeEqualTo true
                component2.canInject<MyComponent>("myComponent2") shouldBeEqualTo true

                component2.canInject<String>() shouldBeEqualTo false
                component2.canInject<MyComponent>("myComponent3") shouldBeEqualTo false
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

                timesSingletonCreated shouldBeEqualTo 1

                val myComponent: MyComponentB<String> by component.inject()
                myComponent.value shouldBeEqualTo "Hello world"

                timesSingletonCreated shouldBeEqualTo 1
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

                timesFirstSingletonCreated shouldBeEqualTo 1
                timesSecondSingletonCreated shouldBeEqualTo 1

                component.injectNow<String>("eagerSingleton1") shouldBeEqualTo "eagerSingleton1 eagerSingleton2"
                component.injectNow<String>("eagerSingleton2") shouldBeEqualTo "eagerSingleton2"
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

            it("circular dependencies with kotlin.lazy should work") {
                val module = Module {

                    singleton { A2(kotlin.lazy { get<B2>() }) }

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

                injection shouldBeEqualTo null
            }

            it("permit internal module bindings") {
                val module = Module {

                    factory(name = "internal", internal = true) { "Hello world" }

                    factory<MyComponent> { MyComponentB<String>(get("internal")) }
                }

                val component = Component(module)

                component.canInject<String>("internal") shouldBeEqualTo false
                component.canInject<MyComponent>() shouldBeEqualTo true

                val injection = component.injectNow<MyComponent>()

                injection shouldBeInstanceOf MyComponentB::class
                (injection as MyComponentB<*>).value shouldBeEqualTo "Hello world"

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

                component.canInject<String?>("singleton") shouldBeEqualTo true
                component.injectNow<String?>("singleton") shouldBeEqualTo null
                component.injectNow<String?>("singleton") shouldBeEqualTo null

                invocations shouldBeEqualTo 1
            }

            it("eagerSingleton injection should allow null values") {
                var invocations = 0
                val module = Module {

                    eagerSingleton<String?>("eagerSingleton") { invocations++; null }
                }

                val component = Component(module)

                component.canInject<String?>("eagerSingleton") shouldBeEqualTo true
                component.injectNow<String?>("eagerSingleton") shouldBeEqualTo null
                component.injectNow<String?>("eagerSingleton") shouldBeEqualTo null

                invocations shouldBeEqualTo 1
            }

            it("custom injection should pass arguments to custom provider") {
                val provider = object : Provider<String> {
                    override fun invoke(context: ComponentContext, arg: Any?) = "Hello $arg"
                }

                val module = Module {
                    custom(name = "TEST", provider = provider)
                }

                val component = Component(module)

                component.custom<String>(name = "TEST", arg = "World") shouldBeEqualTo "Hello World"
            }

            it("inject*OrNull functions should return null for non-declared bindings") {
                val module = Module {

                    factory { MyComponentA() }

                    factory { MyComponentB<String>("value") }
                }

                val component = Component(module)

                component.injectNowOrNull<MyComponentA>() shouldBeInstanceOf MyComponentA::class
                component.injectNowOrNull<MyComponentB<String>>() shouldBeInstanceOf MyComponentB::class
                component.injectNowOrNull<MyComponentC<Any, Any>>() shouldBe null

                val myComponentA by component.injectOrNull<MyComponentA>()
                val myComponentB by component.injectOrNull<MyComponentB<String>>()
                val myComponentC by component.injectOrNull<MyComponentC<Any, Any>>()

                myComponentA shouldBeInstanceOf MyComponentA::class
                myComponentB shouldBeInstanceOf MyComponentB::class
                myComponentC shouldBe null
            }

            it("getOrNull function should return null for non-declared bindings") {
                class MyComponentD(
                    val myComponentA: MyComponentA?,
                    val myComponentB: MyComponentB<String>?
                )

                val module = Module {

                    factory { MyComponentA() }

                    factory { MyComponentD(getOrNull(), getOrNull()) }
                }

                val component = Component(module)

                val myComponentD by component.inject<MyComponentD>()

                myComponentD.myComponentA shouldBeInstanceOf MyComponentA::class
                myComponentD.myComponentB shouldBe null
            }

            it("provider() should return a provider function") {
                val module = Module {

                    factory(name = "factory") { Any() }

                    singleton(name = "singleton") { Any() }

                    factory<() -> Any>(name = "factory provider") { provider(name = "factory") }

                    factory<() -> Any>(name = "singleton provider") { provider(name = "singleton") }
                }

                val component = Component(module)

                val factoryProvider: () -> Any by component.inject(name = "factory provider")
                val singletonProvider: () -> Any by component.inject(name = "singleton provider")

                factoryProvider() shouldNotBeEqualTo factoryProvider()
                singletonProvider() shouldBeEqualTo singletonProvider()
            }
        }
    })

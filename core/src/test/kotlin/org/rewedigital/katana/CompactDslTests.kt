package org.rewedigital.katana

import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompactDslTests : Spek(
    {
        describe("Compact DSL") {

            it("should work") {
                var invocations = 0

                val module = createModule {

                    factory<MyComponent> { MyComponentA() }

                    factory { MyComponentB<MyComponent>(get()) }

                    factory(name = "A") { "Hello" }

                    singleton(name = "B") { invocations++; "World" }
                }

                val component = createComponent(module)

                component.injectNow<MyComponent>() shouldBeInstanceOf MyComponentA::class

                component.injectNow<MyComponentB<MyComponent>>() shouldBeInstanceOf MyComponentB::class

                component.injectNow<String>(name = "A") shouldEqual "Hello"

                component.injectNow<String>(name = "B")
                component.injectNow<String>(name = "B") shouldEqual "World"

                invocations shouldEqual 1
            }
        }
    })

package org.rewedigital.katana

import org.amshove.kluent.shouldBe
import org.rewedigital.katana.dsl.alias
import org.rewedigital.katana.dsl.singleton
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AliasTests : Spek(
    {
        describe("Alias bindings") {

            it("should provide aliases for class bindings") {

                val module = Module {

                    singleton { MyComponentA() }

                    alias<MyComponentA, MyComponent>()
                }

                val component = Component(module)

                val myComponentA: MyComponentA by component.inject()
                val myComponent: MyComponent by component.inject()

                myComponent shouldBe myComponentA
            }

            it("should provide aliases for named bindings") {

                val module = Module {

                    singleton(name = "original name") { MyComponentA() }

                    alias<MyComponentA, MyComponent>(
                        originalName = "original name",
                        name = "alias name"
                    )
                }

                val component = Component(module)

                val myComponentA: MyComponentA by component.inject(name = "original name")
                val myComponent: MyComponent by component.inject(name = "alias name")

                myComponent shouldBe myComponentA
            }
        }
    })

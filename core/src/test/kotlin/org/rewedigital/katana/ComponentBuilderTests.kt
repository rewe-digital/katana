package org.rewedigital.katana

import org.amshove.kluent.shouldEqual
import org.rewedigital.katana.dsl.compact.factory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ComponentBuilderTests : Spek(
    {
        describe("Component Builder") {

            it("should build component with add functions") {
                val module1 = Module {
                    factory("TEST") { "Hello" }
                }

                val component1 = Component(module1)

                val module2 = Module {
                    factory("TEST2") { "World" }
                }

                val component2 = Component.Builder()
                    .addModule(module2)
                    .addDependsOn(component1)
                    .build()

                component2.injectNow<String>("TEST") shouldEqual "Hello"
                component2.injectNow<String>("TEST2") shouldEqual "World"
            }

            it("should build component with set functions") {
                val module1 = Module {
                    factory("TEST") { "Hello" }
                }

                val component1 = Component(module1)

                val module2 = Module {
                    factory("TEST2") { "World" }
                }

                var component2 = Component.Builder()
                    .setModules(module2)
                    .setDependsOn(component1)
                    .build()

                component2.injectNow<String>("TEST") shouldEqual "Hello"
                component2.injectNow<String>("TEST2") shouldEqual "World"

                component2 = Component.Builder()
                    .setModules(listOf(module2))
                    .setDependsOn(listOf(component1))
                    .build()

                component2.injectNow<String>("TEST") shouldEqual "Hello"
                component2.injectNow<String>("TEST2") shouldEqual "World"
            }
        }
    })

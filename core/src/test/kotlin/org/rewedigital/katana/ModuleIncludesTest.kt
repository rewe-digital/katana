package org.rewedigital.katana

import org.amshove.kluent.shouldEqual
import org.rewedigital.katana.dsl.factory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ModuleIncludesTest : Spek(
    {
        val module1 by memoized {
            Module {
                factory(name = "Module1") { "Module1" }
            }
        }
        val module2 by memoized {
            Module {
                factory(name = "Module2") { "Module2" }
            }
        }
        val module3 by memoized {
            Module(includes = listOf(module1, module2)) {
                factory(name = "Module3") { "Module3" }
            }
        }
        val module4 by memoized {
            Module(includes = listOf(module1, module3), declarations = {
                factory(name = "Module4") { "Module4" }
            })
        }
        val module5 by memoized {
            Module(includes = listOf(module1, module2))
        }

        describe("Module includes") {

            it("should include referenced modules") {
                val component = Component(module3)

                component.injectNow<String>("Module1") shouldEqual "Module1"
                component.injectNow<String>("Module2") shouldEqual "Module2"
                component.injectNow<String>("Module3") shouldEqual "Module3"
            }

            it("should work when same modules are included multiple times") {
                val component = Component(module4)

                component.injectNow<String>("Module1") shouldEqual "Module1"
                component.injectNow<String>("Module2") shouldEqual "Module2"
                component.injectNow<String>("Module3") shouldEqual "Module3"
                component.injectNow<String>("Module4") shouldEqual "Module4"
            }

            it("should work when same modules are included multiple times via components") {
                val component1 = Component(module3)
                val component2 = Component(modules = listOf(module3), dependsOn = listOf(component1))

                component2.injectNow<String>("Module1") shouldEqual "Module1"
                component2.injectNow<String>("Module2") shouldEqual "Module2"
                component2.injectNow<String>("Module3") shouldEqual "Module3"
            }

            it("alias modules should be possible") {
                val component = Component(module5)

                component.injectNow<String>("Module1") shouldEqual "Module1"
                component.injectNow<String>("Module2") shouldEqual "Module2"
                component.canInject<String>("Module3") shouldEqual false
            }
        }
    })

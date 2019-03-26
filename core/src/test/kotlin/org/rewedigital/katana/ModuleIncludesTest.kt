package org.rewedigital.katana

import org.amshove.kluent.shouldEqual
import org.rewedigital.katana.dsl.compact.factory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ModuleIncludesTest : Spek(
    {
        val module1 by memoized {
            createModule {
                factory(name = "Module1") { "Module1" }
            }
        }
        val module2 by memoized {
            createModule {
                factory(name = "Module2") { "Module2" }
            }
        }
        val module3 by memoized {
            createModule(includes = listOf(module1, module2)) {
                factory(name = "Module3") { "Module3" }
            }
        }
        val module4 by memoized {
            createModule(includes = listOf(module1, module3)) {
                factory(name = "Module4") { "Module4" }
            }
        }
        val module5 by memoized {
            createModule(includes = listOf(module1, module2))
        }

        describe("Module includes") {

            it("should include referenced modules") {
                val component = createComponent(module3)

                component.injectNow<String>("Module1") shouldEqual "Module1"
                component.injectNow<String>("Module2") shouldEqual "Module2"
                component.injectNow<String>("Module3") shouldEqual "Module3"
            }

            it("should work when same modules are included multiple times") {
                val component = createComponent(module4)

                component.injectNow<String>("Module1") shouldEqual "Module1"
                component.injectNow<String>("Module2") shouldEqual "Module2"
                component.injectNow<String>("Module3") shouldEqual "Module3"
                component.injectNow<String>("Module4") shouldEqual "Module4"
            }

            it("should work when same modules are included multiple times via components") {
                val component1 = createComponent(module3)
                val component2 = createComponent(modules = listOf(module3), dependsOn = listOf(component1))

                component2.injectNow<String>("Module1") shouldEqual "Module1"
                component2.injectNow<String>("Module2") shouldEqual "Module2"
                component2.injectNow<String>("Module3") shouldEqual "Module3"
            }

            it("alias modules should be possible") {
                val component = createComponent(module5)

                component.injectNow<String>("Module1") shouldEqual "Module1"
                component.injectNow<String>("Module2") shouldEqual "Module2"
                component.canInject<String>("Module3") shouldEqual false
            }
        }
    })

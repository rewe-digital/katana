package org.rewedigital.katana

import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ComponentTests : Spek(
    {
        describe("Components") {

            it("should throw exception when no modules or parent components were passed") {
                val fn = {
                    createComponent()
                }

                fn shouldThrow ComponentException::class
            }

            it("should throw exception when all modules are empty") {
                val fn = {
                    val module1 = createModule {
                    }

                    val module2 = createModule {
                    }

                    createComponent(module1, module2)
                }

                fn shouldThrow ComponentException::class
            }
        }
    })

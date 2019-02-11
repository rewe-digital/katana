package org.rewedigital.katana

import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object OverrideTests : Spek(
    {
        describe("Modules with overrides") {

            it("should throw exception when dependencies are overridden in same createModule") {
                val fn = {
                    createModule {

                        factory<MyComponent> { MyComponentA() }

                        factory<MyComponent> { MyComponentA() }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should throw exception when dependencies are overridden in same createModule with identical names") {
                val fn = {
                    createModule {

                        factory("createComponent") { MyComponentA() }

                        factory("createComponent") {
                            MyComponentB("Hello world")
                        }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should throw exception when dependencies are overridden in multiple modules") {
                val module1 = createModule {

                    factory<MyComponent> { MyComponentA() }
                }

                val module2 = createModule {

                    factory<MyComponent> { MyComponentA() }
                }

                val fn = { createComponent(module1, module2) }

                fn shouldThrow OverrideException::class
            }

            it("should throw exception for internal module binding overrides") {
                val fn = {
                    createModule {

                        factory("internal", internal = true) { "Hello world" }

                        factory("internal", internal = true) { "Hello world" }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should not throw exception for internal module binding overrides across module boundaries") {
                val module1 = createModule {

                    factory("internal", internal = true) { "Hello world" }

                    factory { MyComponentA() }
                }

                val module2 = createModule {

                    factory("internal", internal = true) { "Hello world 2" }

                    factory { MyComponentB<String>(get("internal")) }
                }

                val fn = {
                    createComponent(module1, module2)
                }

                fn shouldNotThrow OverrideException::class
            }
        }
    })

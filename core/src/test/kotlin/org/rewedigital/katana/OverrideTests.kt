package org.rewedigital.katana

import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.factory
import org.rewedigital.katana.dsl.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object OverrideTests : Spek(
    {
        describe("Modules with overrides") {

            it("should throw exception when dependencies are overridden in same module") {
                val fn = {
                    Module {

                        factory<MyComponent> { MyComponentA() }

                        factory<MyComponent> { MyComponentA() }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should throw exception when dependencies are overridden in same module with identical names") {
                val fn = {
                    Module {

                        factory("component") { MyComponentA() }

                        factory("component") {
                            MyComponentB("Hello world")
                        }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should throw exception when dependencies are overridden in multiple modules") {
                val module1 = Module {

                    factory<MyComponent> { MyComponentA() }
                }

                val module2 = Module {

                    factory<MyComponent> { MyComponentA() }
                }

                val fn = { Component(module1, module2) }

                fn shouldThrow OverrideException::class
            }

            it("should throw exception for internal module binding overrides") {
                val fn = {
                    Module {

                        factory("internal", internal = true) { "Hello world" }

                        factory("internal", internal = true) { "Hello world" }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should not throw exception for internal module binding overrides across module boundaries") {
                val module1 = Module {

                    factory("internal", internal = true) { "Hello world" }

                    factory { MyComponentA() }
                }

                val module2 = Module {

                    factory("internal", internal = true) { "Hello world 2" }

                    factory { MyComponentB<String>(get("internal")) }
                }

                val fn = {
                    Component(module1, module2)
                }

                fn shouldNotThrow OverrideException::class
            }
        }
    })

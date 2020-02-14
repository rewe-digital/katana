package org.rewedigital.katana

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.factory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TypeErasureTests : Spek(
    {
        describe("Modules with type erasure") {

            it("should throw exception when type erasure kicks in") {
                val fn = {
                    Module {

                        factory {
                            MyComponentB(1337)
                        }

                        factory {
                            MyComponentB("Hello world")
                        }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should work when unique names have been specified") {
                val module = Module {

                    factory("int") {
                        MyComponentB(1337)
                    }

                    factory("string") {
                        MyComponentB("Hello world")
                    }
                }

                val component = Component(module)

                val myComponent1: MyComponentB<Int> by component.inject("int")
                val myComponent2: MyComponentB<String> by component.inject("string")

                myComponent1.value shouldBeEqualTo 1337
                myComponent2.value shouldBeEqualTo "Hello world"
            }
        }
    })
